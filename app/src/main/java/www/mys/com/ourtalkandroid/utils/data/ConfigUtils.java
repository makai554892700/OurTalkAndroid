package www.mys.com.ourtalkandroid.utils.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.activity.MainActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.utils.MD5Utils;
import www.mys.com.ourtalkandroid.utils.ResolverUtils;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.StringUtils;

public class ConfigUtils {

    private static String session;

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    public static String getChatKey(String fromUser, String toUser) {
        return MD5Utils.MD5(fromUser + toUser, false);
    }

    public static void logout() {
        LogUtils.log("logout");
        ResolverUtils.getInstance().saveSetting(StaticParam.USER_INFO, StaticParam.EMPTY);
        ResolverUtils.getInstance().saveSetting(StaticParam.SESSION, StaticParam.EMPTY);
        ResolverUtils.getInstance().saveSetting(StaticParam.ADD_FRIEND, StaticParam.EMPTY);
        ResolverUtils.getInstance().saveSetting(StaticParam.FRIEND_SHIP, StaticParam.EMPTY);
    }

    public static boolean isFriend(Context context, String userName) {
        User user = getUserInfo(context, null);
        if (userName.equals(user.userName)) {
            return true;
        }
        ArrayList<FriendShip> friends = getFriendShip(context, null);
        for (FriendShip friendShip : friends) {
            if (userName.equals(friendShip.toUser)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<AddFriend> acceptAddFriend(Context context, int id) {
        ArrayList<AddFriend> result = getAddFriend(context, null);
        for (AddFriend addFriend : result) {
            if (addFriend.id != null && addFriend.id == id) {
                addFriend.accept = true;
            }
        }
        ResolverUtils.getInstance().saveSetting(StaticParam.ADD_FRIEND, result.toString());
        return result;
    }

    public static ArrayList<AddFriend> getAddFriend(Context context, Back back) {
        if (back != null) {
            updateAddFriend(context, back, 3);
        }
        return AddFriend.JSONArrayStrToArray(AddFriend.class
                , ResolverUtils.getInstance().getStringSetting(StaticParam.ADD_FRIEND));
    }

    public static ArrayList<FriendShip> getFriendShip(Context context, Back back) {
        if (back != null) {
            updateFriendShip(context, back, 3);
        }
        return FriendShip.JSONArrayStrToArray(FriendShip.class
                , ResolverUtils.getInstance().getStringSetting(StaticParam.FRIEND_SHIP));
    }

    public static User getUserInfo(Context context, Back back) {
        if (back != null) {
            updateUserInfo(context, back, 3);
        }
        return ResolverUtils.getInstance().getOperateData(StaticParam.USER_INFO, User.class);
    }

    private static void updateAddFriend(final Context context
            , final Back back, final int retryTimes) {
        if (retryTimes < 1) {
            if (back != null) {
                back.onBack(false);
            }
            return;
        }
        DataUtils.getAddFriends(context, new DataUtils.ListBack<AddFriend>() {
            @Override
            public void onSuccess(ArrayList<AddFriend> response) {
                if (response != null) {
                    mergeAddFriend(context, response);
                    if (back != null) {
                        back.onBack(true);
                    }
                } else {
                    updateAddFriend(context, back, retryTimes - 1);
                }
            }

            @Override
            public void onFailed(Integer status, String message) {
                updateAddFriend(context, back, retryTimes - 1);
            }
        });
    }

    private static void mergeAddFriend(Context context, ArrayList<AddFriend> response) {
        ArrayList<AddFriend> history = getAddFriend(context, null);
        ArrayList<Integer> responseIds = new ArrayList<>();
        for (AddFriend addFriend : response) {
            responseIds.add(addFriend.id);
        }
        ArrayList<Integer> acceptIds = new ArrayList<>();
        ArrayList<Integer> readIds = new ArrayList<>();
        for (int i = 0; i < history.size(); i++) {
            if (responseIds.contains(history.get(i).id)) {
                if (history.get(i).accept) {
                    acceptIds.add(history.get(i).id);
                }
                if (history.get(i).read) {
                    readIds.add(history.get(i).id);
                }
                history.remove(i--);
            }
        }
        response.addAll(history);
        for (AddFriend addFriend : response) {
            if (acceptIds.contains(addFriend.id)) {
                addFriend.accept = true;
            }
            if (readIds.contains(addFriend.id)) {
                addFriend.read = true;
            }
        }
        ResolverUtils.getInstance().saveSetting(StaticParam.ADD_FRIEND
                , response.toString());
    }

    private static void updateFriendShip(final Context context, final Back back, final int retryTimes) {
        if (retryTimes < 1) {
            if (back != null) {
                back.onBack(false);
            }
            return;
        }
        DataUtils.getFriends(context, new DataUtils.ListBack<FriendShip>() {
            @Override
            public void onSuccess(ArrayList<FriendShip> response) {
                if (response != null) {
                    ResolverUtils.getInstance().saveSetting(StaticParam.FRIEND_SHIP
                            , response.toString());
                    if (back != null) {
                        back.onBack(true);
                    }
                } else {
                    updateFriendShip(context, back, retryTimes - 1);
                }
            }

            @Override
            public void onFailed(Integer status, String message) {
                updateFriendShip(context, back, retryTimes - 1);
            }
        });
    }

    private static void updateUserInfo(final Context context, final Back back, final int retryTimes) {
        if (retryTimes < 1) {
            if (back != null) {
                back.onBack(false);
            }
            return;
        }
        String session = ResolverUtils.getInstance().getStringSetting(StaticParam.SESSION);
        if (StringUtils.isEmpty(session)) {
            if (back != null) {
                back.onBack(false);
            }
            LogUtils.log("updateUserInfo user not login.");
            return;
        }
        DataUtils.getUserInfo(context, new DataUtils.BodyBack<User>() {
            @Override
            public void onSuccess(User response) {
                if (response != null) {
                    ResolverUtils.getInstance().saveSetting(StaticParam.USER_INFO
                            , response.toString());
                    if (back != null) {
                        back.onBack(true);
                    }
                } else {
                    updateUserInfo(context, back, retryTimes - 1);
                }
            }

            @Override
            public void onFailed(Integer status, String message) {
                updateUserInfo(context, back, retryTimes - 1);
            }
        });
    }

    public static boolean checkLogin(final Activity activity) {
        if (activity == null) {
            return false;
        }
        session = ResolverUtils.getInstance().getStringSetting(StaticParam.SESSION);
//        LogUtils.log("session=" + session);
        boolean result = !StringUtils.isEmpty(session);
        if (result) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                LogUtils.log("e=" + e);
            }
            activity.finish();
        }
        return result;
    }

    public interface Back {
        public void onBack(boolean success);
    }

}
