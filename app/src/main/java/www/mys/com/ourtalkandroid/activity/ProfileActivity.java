package www.mys.com.ourtalkandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseAppbarLoadingActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.AddFriend;
import www.mys.com.ourtalkandroid.pojo.FriendShip;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.chat.BaseChatPOJO;
import www.mys.com.ourtalkandroid.utils.chat.ChatServiceUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.data.DataUtils;

public class ProfileActivity extends BaseAppbarLoadingActivity {

    @ViewDesc(viewId = R.id.head_img)
    public ImageView headImg;
    @ViewDesc(viewId = R.id.item_title)
    public TextView itemTitle;
    @ViewDesc(viewId = R.id.item_desc)
    public TextView itemDesc;
    @ViewDesc(viewId = R.id.line_3)
    public View line3;
    @ViewDesc(viewId = R.id.item_1)
    public TextView item1;
    @ViewDesc(viewId = R.id.item_2)
    public TextView item2;
    private FriendShip friendShip;
    private User user;
    private boolean isFriend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String data = intent.getStringExtra(StaticParam.DATA);
        friendShip = new FriendShip(data);
        isFriend = ConfigUtils.isFriend(getApplicationContext(), friendShip.toUser);
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        user = ConfigUtils.getUserInfo(getApplicationContext(), success -> {
            if (success) {
                user = ConfigUtils.getUserInfo(getApplicationContext(), null);
            }
        });
    }

    private void initView() {
        if (isFriend) {
            item1.setText(R.string.send_message);
        } else {
            item1.setText(R.string.add_user);
            item2.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
        }
        itemTitle.setText(friendShip.descName);
        itemDesc.setText(
                getString(R.string.nick_name, friendShip.descName)
                        + "\n"
                        + getString(R.string.user_name, friendShip.toUser)
        );
    }

    private void initEvent() {
        if (isFriend) {
            item1.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), TalkActivity.class);
                intent.putExtra(StaticParam.DATA, friendShip.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    LogUtils.log("start activity error.e=" + e);
                }
            });
        } else {
            item1.setOnClickListener(view -> {
                addFriend();
            });
        }
    }

    private void addFriend() {
        DataUtils.BodyBack<AddFriend> bodyBack = new DataUtils.BodyBack<AddFriend>() {
            @Override
            public void onSuccess(AddFriend response) {
                LogUtils.log("addFriend success.response=" + response);
                ChatServiceUtils.getInstance().send(
                        ChatServiceUtils.ADD_FRIENDS
                        , new BaseChatPOJO(
                                BaseChatPOJO.Type.ADD_FRIENDS
                                , ConfigUtils.getChatKey(user.userName, friendShip.toUser)
                                , user.userName
                                , friendShip.toUser
                                , null, StaticParam.EMPTY
                        ).toString()
                );
                finish();
            }

            @Override
            public void onFailed(Integer status, String message) {
                LogUtils.log("addFriend failed status=" + status + ";message=" + message);
            }
        };
        DataUtils.addFriend(getApplicationContext(), friendShip.toUser, bodyBack);
    }

    @Override
    public String getAppBarTitle() {
        return StaticParam.EMPTY;
    }

    @Override
    public int getLeftIcon() {
        return R.drawable.ic_back;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }
}
