
package www.mys.com.ourtalkandroid.utils.chat;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.widget.Toast;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import www.mys.com.ourtalkandroid.ChatCallBack;
import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.activity.MainActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.NotificationUtils;
import www.mys.com.ourtalkandroid.utils.ResolverUtils;
import www.mys.com.ourtalkandroid.utils.ToolUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.db.DBChatListUtils;
import www.mys.com.ourtalkandroid.utils.db.DBTalkInfoUtils;
import www.mys.com.ourtalkandroid.utils.db.model.DBChatList;
import www.mys.com.ourtalkandroid.utils.db.model.DBTalkInfo;

import java.net.URI;

public class ChatClientUtils {

    private final String url;
    private final String userKey;
    private final Context context;
    private final EventLoopGroup eventLoopGroup;
    public final RemoteCallbackList<ChatCallBack> chatCallBack;
    private boolean running;
    private Channel channel;

    public SendBack sendText(BaseChatPOJO baseChatPOJO) {
        LogUtils.log("ChatClientUtils sendText baseChatPOJO=" + baseChatPOJO);
        if (baseChatPOJO == null) {
            return new SendBack(SendBack.STATE.FAILED, "baseChatPOJO is null.", null);
        }
        if (channel == null) {
            return new SendBack(SendBack.STATE.FAILED, "user not connect.", null);
        }
        final StringBuilder result = new StringBuilder();
        ChatUtils.sendData(channel
                , baseChatPOJO
                , future -> {
                    LogUtils.log("operationComplete future=" + future);
                    LogUtils.log("operationComplete isSuccess=" + future.isSuccess());
                    if (future.isSuccess()) {
                        result.append(ChatUtils.SUCCESS);
                    } else {
                        result.append(ChatUtils.FAILED);
                    }
                });
        int times = 0;
        while (result.length() == 0 && times++ < 600) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        if (ChatUtils.SUCCESS.equals(result.toString())) {
            return new SendBack(SendBack.STATE.SUCCESS, "send success.", baseChatPOJO.toString());
        } else {
            return new SendBack(SendBack.STATE.FAILED, "send failed.", baseChatPOJO.toString());
        }
    }

    public static class MockClientHandler extends SimpleChannelInboundHandler<Object> {

        private final WebSocketClientHandshaker webSocketClientHandshaker;
        private final ChatClientUtils chatClientUtils;
        private final String userKey;
        private DBChatListUtils dbChatListUtils;
        private DBTalkInfoUtils dbTalkInfoUtils;

        private void onTextBack(TextWebSocketFrame textFrame) {
            final String text = textFrame.text();
            LogUtils.log("onTextBack text=" + text);
            if (chatClientUtils.context != null) {
                final BaseChatPOJO baseChatPOJO = new BaseChatPOJO(text);
                LogUtils.log("baseChatPOJO=" + baseChatPOJO);
                Handler handlerThree;
                switch (baseChatPOJO.type) {
                    case BaseChatPOJO.Type.TALK_USER:
                    case BaseChatPOJO.Type.TALK_GROUP:
                        DBChatList dbChatList = dbChatListUtils.getDBChatListByKey(
                                ConfigUtils.getChatKey(userKey, baseChatPOJO.fromKey));
                        if (dbChatList == null) {
                            dbChatList = new DBChatList(
                                    DBTalkInfo.Type.TALK_USER
                                    , 1
                                    , ConfigUtils.getChatKey(userKey, baseChatPOJO.fromKey)
                                    , baseChatPOJO.fromKey, baseChatPOJO.fromKey, baseChatPOJO.fromKey
                                    , baseChatPOJO.data
                                    , System.currentTimeMillis()
                            );
                        } else {
                            dbChatList.unReadCount += 1;
                            dbChatList.msgDesc = baseChatPOJO.data;
                            dbChatList.msgTime = System.currentTimeMillis();
                        }
                        dbChatListUtils.saveDBChatList(dbChatList);
                        dbTalkInfoUtils.saveDBTalkInfo(new DBTalkInfo(
                                DBTalkInfo.Type.TALK_USER, DBTalkInfo.TalkType.FROM
                                , ConfigUtils.getChatKey(userKey, baseChatPOJO.fromKey)
                                , baseChatPOJO.fromKey, userKey
                                , baseChatPOJO.atUsers, baseChatPOJO.data, System.currentTimeMillis()
                        ));
                        handlerThree = new Handler(Looper.getMainLooper());
                        handlerThree.post(() -> {
                            NotificationUtils.shoNotification(MainActivity.class
                                    , NotificationUtils.Type.MESSAGE, baseChatPOJO.fromKey
                                    , baseChatPOJO.data);
                            int len = chatClientUtils.chatCallBack.beginBroadcast();
                            for (int i = 0; i < len; i++) {
                                try {
                                    chatClientUtils.chatCallBack.getBroadcastItem(i)
                                            .onBack(ChatServiceUtils.RECEIVE_MESSAGE, text);
                                } catch (Exception e) {
                                    LogUtils.log("back error.e=" + e);
                                }
                            }
                            chatClientUtils.chatCallBack.finishBroadcast();
                        });
                        break;
                    case BaseChatPOJO.Type.ADD_FRIENDS:
                        handlerThree = new Handler(Looper.getMainLooper());
                        handlerThree.post(() -> {
                            NotificationUtils.shoNotification(MainActivity.class
                                    , NotificationUtils.Type.FRIEND_REQUEST
                                    , baseChatPOJO.fromKey
                                    , chatClientUtils.context.getString(R.string.friend_request));
                            int len = chatClientUtils.chatCallBack.beginBroadcast();
                            for (int i = 0; i < len; i++) {
                                try {
                                    chatClientUtils.chatCallBack.getBroadcastItem(i)
                                            .onBack(ChatServiceUtils.ADD_FRIENDS, text);
                                } catch (Exception e) {
                                    LogUtils.log("back error.e=" + e);
                                }
                            }
                            chatClientUtils.chatCallBack.finishBroadcast();
                        });
                        break;
                }

            }
        }

        private void onBinaryBack(BinaryWebSocketFrame binaryFrame) {

        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            LogUtils.log("channelRead0 msg" + msg);
            Channel ch = ctx.channel();
            FullHttpResponse response;
            if (!webSocketClientHandshaker.isHandshakeComplete()) {
                handShake(ctx, msg);
            } else if (msg instanceof FullHttpResponse) {
                response = (FullHttpResponse) msg;
                throw new IllegalStateException("Unexpected FullHttpResponse (getStatus="
                        + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
            } else {
                WebSocketFrame frame = (WebSocketFrame) msg;
                if (frame instanceof TextWebSocketFrame) {
                    onTextBack((TextWebSocketFrame) frame);
                } else if (frame instanceof BinaryWebSocketFrame) {
                    onBinaryBack((BinaryWebSocketFrame) frame);
                } else if (frame instanceof PongWebSocketFrame) {
                    PongWebSocketFrame pongFrame = (PongWebSocketFrame) frame;
                    LogUtils.log("WebSocket Client received pong data=" + new String(pongFrame.content().array()));
                } else if (frame instanceof CloseWebSocketFrame) {
                    CloseWebSocketFrame closeFrame = (CloseWebSocketFrame) frame;
                    LogUtils.log("receive close frame data=" + new String(closeFrame.content().array()));
                    ch.close();
                }
            }
        }

        public MockClientHandler(WebSocketClientHandshaker webSocketClientHandshaker
                , ChatClientUtils chatClientUtils, String userKey) {
            this.chatClientUtils = chatClientUtils;
            if (chatClientUtils.context != null) {
                dbChatListUtils = new DBChatListUtils(chatClientUtils.context);
                dbTalkInfoUtils = new DBTalkInfoUtils(chatClientUtils.context);
            }
            this.webSocketClientHandshaker = webSocketClientHandshaker;
            this.userKey = userKey;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            LogUtils.log("channelActive");
            chatClientUtils.channel = ctx.channel();
            webSocketClientHandshaker.handshake(ctx.channel());
            ToolUtils.saveServiceState(true);
            Handler handlerThree = new Handler(Looper.getMainLooper());
            handlerThree.post(() -> {
                int len = chatClientUtils.chatCallBack.beginBroadcast();
                for (int i = 0; i < len; i++) {
                    try {
                        chatClientUtils.chatCallBack.getBroadcastItem(i).onBack(
                                ChatServiceUtils.STATE_BACK, StaticParam.EMPTY);
                    } catch (Exception e) {
                        LogUtils.log("back error.e=" + e);
                    }
                }
                chatClientUtils.chatCallBack.finishBroadcast();
            });
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            LogUtils.log("channelInactive");
            ToolUtils.saveServiceState(false);
            chatClientUtils.running = false;
            chatClientUtils.channel = null;
            Handler handlerThree = new Handler(Looper.getMainLooper());
            handlerThree.post(() -> {
                int len = chatClientUtils.chatCallBack.beginBroadcast();
                for (int i = 0; i < len; i++) {
                    try {
                        chatClientUtils.chatCallBack.getBroadcastItem(i).onBack(
                                ChatServiceUtils.STATE_BACK, StaticParam.EMPTY);
                    } catch (Exception e) {
                        LogUtils.log("back error.e=" + e);
                    }
                }
                chatClientUtils.chatCallBack.finishBroadcast();
            });
        }

        private void handShake(ChannelHandlerContext ctx, Object msg) {
            try {
                FullHttpResponse response = (FullHttpResponse) msg;
                webSocketClientHandshaker.finishHandshake(ctx.channel(), response);
                ctx.newPromise().setSuccess();
                LogUtils.log("WebSocket Client connected! response headers[sec-websocket-extensions]:{}" + response.headers());
                ChatUtils.sendData(ctx.channel()
                        , new BaseChatPOJO(BaseChatPOJO.Type.HANDSHAKE, null
                                , null, null, null, userKey)
                        , future -> {
                            LogUtils.log("send userKey isSuccess:" + future.isSuccess());
                        });
            } catch (WebSocketHandshakeException var7) {
                FullHttpResponse res = (FullHttpResponse) msg;
                String errorMsg = String.format("WebSocket Client failed to connect,status:%s,reason:%s", res.status()
                        , res.content().toString(CharsetUtil.UTF_8));
                ctx.newPromise().setFailure(new Exception(errorMsg));
            }
        }

    }

    public static class MockClientInitializer extends ChannelInitializer<SocketChannel> {
        private MockClientHandler mockClientHandler;

        MockClientInitializer(MockClientHandler mockClientHandler) {
            this.mockClientHandler = mockClientHandler;
        }

        @Override
        protected void initChannel(SocketChannel channel) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 1024));
            pipeline.addLast(mockClientHandler);
        }
    }

    public void stop() {
        LogUtils.log("stop service.");
        running = false;
        eventLoopGroup.shutdownGracefully();
    }

    public ChatClientUtils(Context context, String userKey, String url
            , RemoteCallbackList<ChatCallBack> chatCallBack) {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.context = context;
        this.userKey = userKey;
        this.url = url;
        this.chatCallBack = chatCallBack;
        new Thread(() -> {
            init();
        }).start();
    }

    public boolean isRunning() {
        return running;
    }

    private void init() {
        running = true;
        try {
            URI uri = new URI(url);
            Bootstrap bootstrap = new Bootstrap();
            MockClientHandler webSocketClientHandler = new MockClientHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(uri
                            , WebSocketVersion.V13
                            , null
                            , false
                            , new DefaultHttpHeaders())
                    , this
                    , userKey);
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                    handler(new MockClientInitializer(webSocketClientHandler));
            Channel channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            LogUtils.log("ChatClientUtils init error.e=" + e);
            eventLoopGroup.shutdownGracefully();
            running = false;
        }
    }

    public static void main(String[] args) {
        ChatClientUtils chatClientUtils = new ChatClientUtils(
                null, "user1"
                , "ws://www.demo.com:5000/ws", null
        );
        while (chatClientUtils.channel == null) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
        }
    }

}
