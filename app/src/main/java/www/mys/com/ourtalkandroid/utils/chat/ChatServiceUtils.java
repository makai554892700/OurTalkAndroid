package www.mys.com.ourtalkandroid.utils.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.annotation.NonNull;

import www.mys.com.ourtalkandroid.ChatCallBack;
import www.mys.com.ourtalkandroid.ChatRegister;
import www.mys.com.ourtalkandroid.service.ChatService;
import www.mys.com.ourtalkandroid.utils.LogUtils;

public class ChatServiceUtils {

    public static final int CONNECT_BACK = 10085;
    public static final int START_CLIENT = 10086;
    public static final int STOP_CLIENT = 10087;
    public static final int SENT_MESSAGE = 10088;
    public static final int RECEIVE_MESSAGE = 10089;
    public static final int STATE_BACK = 10091;
    public static final int ADD_FRIENDS = 10092;
    private static final ChatServiceUtils chatServiceUtils = new ChatServiceUtils();
    private ChatRegister chatRegister;
    private ChatCallBack chatCallBack;

    public void bindService(Context context, @NonNull ChatCallBack chatCallBack) {
        LogUtils.log(" bindService context=" + context);
        this.chatCallBack = chatCallBack;
        Intent intent = new Intent(context, ChatService.class);
        try {
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            LogUtils.log("bindService error.e=" + e);
        }
    }

    public void unbindService(Context context) {
        LogUtils.log("unbindService context=" + context);
        if (null == chatRegister) {
            return;
        }
        try {
            if (null != chatCallBack) {
                chatRegister.unRegister(chatCallBack);
            }
        } catch (Exception e) {
            LogUtils.log("unRegister service error.e=" + e);
        }
        try {
            context.unbindService(connection);
        } catch (Exception e) {
            LogUtils.log("unbindService error.e=" + e);
        }
        chatRegister = null;
        chatCallBack = null;
    }

    public int send(int type, String data) {
        LogUtils.log("send message.type=" + type + ";data=" + data + ";chatRegister=" + chatRegister);
        if (null == chatRegister) {
            return -1;
        }
        try {
            return chatRegister.send(type, data);
        } catch (Exception e) {
            LogUtils.log("send message error.e=" + e);
            return -1;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtils.log("onServiceConnected");
            chatRegister = ChatRegister.Stub.asInterface(iBinder);
            try {
                chatRegister.register(chatCallBack);
                chatCallBack.onBack(CONNECT_BACK, "true");
            } catch (Exception e) {
                LogUtils.log("register service error.e=" + e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                chatCallBack.onBack(CONNECT_BACK, "false");
            } catch (Exception e) {
                LogUtils.log("register service error.e=" + e);
            }
            chatRegister = null;
            chatCallBack = null;
        }
    };

    public static ChatServiceUtils getInstance() {
        return chatServiceUtils;
    }

    private ChatServiceUtils() {
    }

}
