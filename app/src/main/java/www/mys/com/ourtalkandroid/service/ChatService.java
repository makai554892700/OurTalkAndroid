package www.mys.com.ourtalkandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import www.mys.com.ourtalkandroid.ChatCallBack;
import www.mys.com.ourtalkandroid.ChatRegister;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.ResolverUtils;
import www.mys.com.ourtalkandroid.utils.StringUtils;
import www.mys.com.ourtalkandroid.utils.chat.BaseChatPOJO;
import www.mys.com.ourtalkandroid.utils.chat.ChatClientUtils;
import www.mys.com.ourtalkandroid.utils.chat.ChatServiceUtils;
import www.mys.com.ourtalkandroid.utils.chat.SendBack;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;

public class ChatService extends Service {

    private ChatClientUtils chatClientUtils;
    private RemoteCallbackList<ChatCallBack> callbackList;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(() -> {
            do {
                startClient();
                ConfigUtils.sleep(10 * 1000);
            } while (true);
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        callbackList = new RemoteCallbackList<>();
        return chatRegister;
    }

    private synchronized void startClient() {
        User user = ResolverUtils.getInstance().getOperateData(StaticParam.USER_INFO, User.class);
        if (user == null || StringUtils.isEmpty(user.userName)) {
            LogUtils.log("startClient user=" + user);
            stopClient();
        } else {
            if (chatClientUtils == null || !chatClientUtils.isRunning()) {
                while (callbackList == null) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
                chatClientUtils = new ChatClientUtils(getApplicationContext()
                        , user.userName, "ws://www.demo.com:5000/ws"
                        , callbackList);
            }
        }
    }

    ChatRegister.Stub chatRegister = new ChatRegister.Stub() {
        @Override
        public void register(ChatCallBack callback) throws RemoteException {
            LogUtils.log("register");
            if (callback == null) {
                return;
            }
            callbackList.register(callback);
        }

        @Override
        public void unRegister(ChatCallBack callback) throws RemoteException {
            LogUtils.log("unRegister");
            if (callback == null) {
                return;
            }
            callbackList.unregister(callback);
        }

        @Override
        public int send(int type, String data) throws RemoteException {
            LogUtils.log("chatRegister send type:" + type + ";data:" + data);
            BaseChatPOJO baseChatPOJO;
            switch (type) {
                case ChatServiceUtils.START_CLIENT:
                    startClient();
                    break;
                case ChatServiceUtils.STOP_CLIENT:
                    stopClient();
                    break;
                case ChatServiceUtils.SENT_MESSAGE:
                case ChatServiceUtils.ADD_FRIENDS:
                    baseChatPOJO = new BaseChatPOJO(data);
                    if (chatClientUtils != null) {
                        SendBack sendBack = chatClientUtils.sendText(baseChatPOJO);
                        return sendBack.code;
                    } else {
                        LogUtils.log("chatClientUtils is null.");
                    }
                    break;
                default:
                    LogUtils.log("unknow message type.");
            }
            return -1;
        }
    };

    private synchronized void stopClient() {
        LogUtils.log("stopClient");
        if (chatClientUtils != null) {
            chatClientUtils.stop();
            chatClientUtils = null;
        }
    }

}
