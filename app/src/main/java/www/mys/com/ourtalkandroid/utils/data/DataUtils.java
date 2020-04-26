package www.mys.com.ourtalkandroid.utils.data;

import android.content.Context;
import android.content.Intent;

import com.mayousheng.www.basepojo.BasePoJo;
import com.mayousheng.www.httputils.HttpUtils;

import org.json.JSONArray;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.pojo.ObjResult;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.encrypt.EncryptUtils;

public class DataUtils {

    public static void register(final Context context, final String userName, final String pass
            , final BodyBack<User> bodyBack) {
        commonGetBody(context, StaticParam.API_REGISTER, new User(userName, pass, userName).toString()
                , new BodyBack<User>() {
                    @Override
                    public void onSuccess(User response) {
                        login(context, userName, pass, bodyBack);
                    }

                    @Override
                    public void onFailed(Integer status, String message) {
                        if (bodyBack != null) {
                            bodyBack.onFailed(status, message);
                        }
                    }
                }, User.class);
    }

    public static void login(Context context, String userName, String pass, BodyBack<User> bodyBack) {
        commonGetBody(context, StaticParam.API_LOGIN, new User(userName, pass, userName).toString()
                , bodyBack, User.class);
    }

    public static void getUserInfo(Context context, BodyBack<User> bodyBack) {
        commonGetBody(context, StaticParam.API_GET_USER_INFO, bodyBack, User.class);
    }

    public static void queryUser(Context context, String userName, BodyBack<User> bodyBack) {
        commonGetBody(context, String.format(StaticParam.API_QUERY_USER, userName)
                , bodyBack, User.class);
    }

    public static void logout(final Context context, StringBack stringBack) {
        commonGetBody(context, StaticParam.API_LOGOUT, stringBack);
    }

    public static void logout(final Context context) {
        logout(context, new StringBack() {
            @Override
            public void onSuccess(String response) {
                ConfigUtils.logout();
                Intent intent = new Intent();
                intent.setAction(StaticParam.EXIT_APP);
                context.sendBroadcast(intent);
            }

            @Override
            public void onFailed(Integer status, String message) {
                LogUtils.log("logout failed.status=" + status + ";message=" + message);
            }
        });
    }

    public static void addFriend(Context context, String userName, BodyBack<AddFriend> bodyBack) {
        AddFriend addFriend = new AddFriend(null);
        User user = ConfigUtils.getUserInfo(context, null);
        addFriend.fromUser = user.userName;
        addFriend.toUser = userName;
        commonGetBody(context, StaticParam.API_ADD_FRIEND, addFriend.toString()
                , bodyBack, AddFriend.class);
    }

    public static void update(Context context, String nickName, BodyBack<User> bodyBack) {
        User user = new User(null);
        user.nickName = nickName;
        commonGetBody(context, StaticParam.API_UPDATE, user.toString()
                , bodyBack, User.class);
    }

    public static void getFriends(Context context, ListBack<FriendShip> listBack) {
        commonGetList(context, StaticParam.API_GET_FRIEND, listBack, FriendShip.class);
    }

    public static void getAddFriends(Context context, ListBack<AddFriend> listBack) {
        commonGetList(context, StaticParam.API_GET_FRIEND_REQUEST, listBack, AddFriend.class);
    }

    public static void accessFriend(Context context, Integer addFriendId, StringBack stringBack) {
        commonGetBody(context, String.format(StaticParam.API_ACCESS_FRIEND, addFriendId)
                , stringBack);
    }

    public static void refuseFriend(Context context, Integer addFriendId, StringBack stringBack) {
        commonGetBody(context, String.format(StaticParam.API_REFUSE_FRIEND, addFriendId)
                , stringBack);
    }

    private static <T extends BasePoJo> void commonGetBody(Context context, final String url
            , final StringBack bodyBack) {
        final HttpUtils.IWebSessionBack iWebSessionBack = getIWebSessionBack(context, bodyBack, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().getURLResponse(url, JsonHeardUtils.getInstance().getHeard()
                        , iWebSessionBack);
            }
        }).start();
    }

    private static <T extends BasePoJo> void commonGetBody(Context context, final String url
            , final BodyBack<T> bodyBack, Class<T> clazz) {
        final HttpUtils.IWebSessionBack iWebSessionBack = getIWebSessionBack(context, bodyBack, clazz);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().getURLResponse(url, JsonHeardUtils.getInstance().getHeard()
                        , iWebSessionBack);
            }
        }).start();
    }

    private static <T extends BasePoJo> void commonGetList(Context context, final String url
            , final ListBack<T> listBack, final Class<T> clazz) {
        final HttpUtils.IWebSessionBack iWebSessionBack = getIWebSessionBack(context, listBack, clazz);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().getURLResponse(url, JsonHeardUtils.getInstance().getHeard()
                        , iWebSessionBack);
            }
        }).start();
    }

    private static <T extends BasePoJo> void commonGetBody(Context context, final String url, String data
            , StringBack bodyBack) {
        if (data == null) {
            data = "";
        }
        final byte[] requestData = EncryptUtils.encrypt(data).getBytes();
        final HttpUtils.IWebSessionBack iWebSessionBack = getIWebSessionBack(context, bodyBack, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().postURLResponse(url, JsonHeardUtils.getInstance().getHeard()
                        , requestData, iWebSessionBack);
            }
        }).start();
    }

    private static <T extends BasePoJo> void commonGetBody(Context context, final String url, String data
            , final BodyBack<T> bodyBack, final Class<T> clazz) {
        if (data == null) {
            data = "";
        }
        final byte[] requestData = EncryptUtils.encrypt(data).getBytes(StandardCharsets.UTF_8);
        final HttpUtils.IWebSessionBack iWebSessionBack = getIWebSessionBack(context, bodyBack, clazz);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().postURLResponse(url, JsonHeardUtils.getInstance().getHeard()
                        , requestData, iWebSessionBack);
            }
        }).start();
    }

    private static <T extends BasePoJo> void commonGetList(Context context, final String url, String data
            , final ListBack<T> listBack, final Class<T> clazz) {
        if (data == null) {
            data = "";
        }
        final byte[] requestData = EncryptUtils.encrypt(data).getBytes();
        final HttpUtils.IWebSessionBack iWebSessionBack = getIWebSessionBack(context, listBack, clazz);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().postURLResponse(url, JsonHeardUtils.getInstance().getHeard()
                        , requestData, iWebSessionBack);
            }
        }).start();
    }

    private static <T extends BasePoJo> HttpUtils.IWebSessionBack getIWebSessionBack(
            final Context context, final Back dataBack, final Class<T> clazz) {
        return new HttpUtils.IWebSessionBack() {
            @Override
            public void onCallback(int status, String message, Map<String, List<String>> heard
                    , String sessionId, byte[] data) {
                JsonHeardUtils.getInstance().addSession(sessionId);
//                LogUtils.log("status=" + status + ";message=" + message
//                        + "\nsessionId=" + sessionId
//                        + "\ndata=" + (data == null ? "null" : new String(data))
//                );
                String dataStr;
                if (data != null) {
                    dataStr = new String(data);
                    if (!dataStr.startsWith("{")) {
                        if (dataStr.startsWith("\"") && dataStr.endsWith("\"")) {
//                            LogUtils.log("start with \"");
                            dataStr = dataStr.substring(1, dataStr.length() - 1);
                        }
                    }

                    if (!dataStr.startsWith("{")) {
                        dataStr = EncryptUtils.decrypt(dataStr);
                    }
                } else {
                    dataStr = StaticParam.EMPTY;
                }
//                LogUtils.log("status=" + status + ";message=" + message
//                        + "\nsessionId=" + sessionId
//                        + "\ndata=" + (data == null ? "null" : new String(data))
//                        + "\ndataStr=" + dataStr
//                );
                ObjResult objResult = new ObjResult<String>(dataStr);
                if (status == 200) {
                    if (objResult.code == 200) {
//                        LogUtils.log("request success.dataStr=" + dataStr);
                        if (dataBack != null) {
                            if (dataBack instanceof StringBack) {
                                ((StringBack) dataBack).onSuccess(objResult.data == null
                                        ? null : objResult.data.toString());
                            } else if (dataBack instanceof BodyBack) {
                                ((BodyBack) dataBack).onSuccess(objResult.data == null
                                        ? null : BasePoJo.strToObject(clazz, objResult.data.toString()));
                            } else if (dataBack instanceof ListBack) {
                                ArrayList<T> result = new ArrayList<>();
                                try {
                                    JSONArray jsonArray = new JSONArray(objResult.data.toString());
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        result.add(BasePoJo.strToObject(clazz, jsonArray.optString(i)));
                                    }
                                } catch (Exception e) {
                                    LogUtils.log("jsonArray format error.e=" + e);
                                }
                                ((ListBack) dataBack).onSuccess(result);
                            }
                        }
                    } else if (objResult.code == StaticParam.NOT_LOGIN_CODE) {
                        LogUtils.log("request failed logout.dataStr=" + dataStr);
                        Intent intent = new Intent();
                        intent.setAction(StaticParam.EXIT_APP);
                        context.sendBroadcast(intent);
                    } else {
                        if (dataBack != null) {
                            LogUtils.log("request failed.dataStr=" + dataStr);
                            dataBack.onFailed(objResult.code, objResult.message);
                        }
                    }
                } else {
                    if (dataBack != null) {
                        dataBack.onFailed(status, message);
                    }
                }
            }

            @Override
            public void onFail(int status, String message) {
                if (dataBack != null) {
                    dataBack.onFailed(status, message);
                }
                LogUtils.log("status=" + status + ";message=" + message);
            }
        };
    }

    public interface ListBack<T extends BasePoJo> extends Back {
        public void onSuccess(ArrayList<T> response);
    }

    public interface StringBack extends Back {
        public void onSuccess(String response);
    }

    public interface BodyBack<T extends BasePoJo> extends Back {
        public void onSuccess(T response);
    }

    public static interface Back {
        public void onFailed(Integer status, String message);
    }

}
