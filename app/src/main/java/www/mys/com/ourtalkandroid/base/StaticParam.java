package www.mys.com.ourtalkandroid.base;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import www.mys.com.ourtalkandroid.utils.MD5Utils;
import www.mys.com.ourtalkandroid.utils.ToolUtils;

public class StaticParam {

    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(2000, 20000
            , 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(2000));
    public static final RecyclerView.ItemDecoration DEFAULT_ITEM_DECORATION = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, 1);
        }
    };
    public static final RecyclerView.ItemDecoration ITEM_DECORATION = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, 0);
        }
    };
    public static final String EMPTY = "";
    public static final String ENTER = "\n";
    public static final String SESSION = "session";
    public static final String USER_INFO = "user_info";
    public static final String FRIEND_SHIP = "friend_ship";
    public static final String ADD_FRIEND = "add_friend";
    public static final String DATA = "activity_data";
    public static final String EXIT_APP = "exit_app";
    public static final String SERVICE_STATE = "service_state";
    public static final String NOTIFICATION_TYPE = "notification_type";       //通知请求码

    public static final int NOT_LOGIN_CODE = 10086;
    public static final int PAGE_COUNT = 10;

    public static final String PACKAGE_NAME = "www.mys.com.ourtalkandroid";
    public static final int DEVICE_ID = 123456;

    public static final String ENCRYPT_CODE = MD5Utils.MD5("http://imgs.demo.com", false);
    public static final String BASE_HOST = "https://www.demo.com";
    public static final String API_START = BASE_HOST + "/api/basesb";
    public static final String API_LOGIN = API_START + "/user/login";
    public static final String API_LOGOUT = API_START + "/user/logout";
    public static final String API_REGISTER = API_START + "/user/register";
    public static final String API_UPDATE = API_START + "/user/update";
    public static final String API_GET_USER_INFO = API_START + "/user/getUserInfo";
    public static final String API_QUERY_USER = API_START + "/user/queryUser/%s";
    public static final String API_ADD_FRIEND = API_START + "/friend/addFriend";
    public static final String API_GET_FRIEND = API_START + "/friend/getFriends";
    public static final String API_GET_FRIEND_REQUEST = API_START + "/friend/getFriendRequest";
    public static final String API_ACCESS_FRIEND = API_START + "/friend/accessFriend/%s";
    public static final String API_REFUSE_FRIEND = API_START + "/friend/refuseFriend/%s";

}
