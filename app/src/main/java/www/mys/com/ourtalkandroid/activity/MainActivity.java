package www.mys.com.ourtalkandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.mayousheng.www.initview.ViewDesc;

import java.util.ArrayList;
import java.util.List;

import www.mys.com.ourtalkandroid.ChatCallBack;
import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.adapter.MainAdapter;
import www.mys.com.ourtalkandroid.base.BaseAppbarLoadingActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.fragment.ChatFragment;
import www.mys.com.ourtalkandroid.fragment.ContactFragment;
import www.mys.com.ourtalkandroid.fragment.FindFragment;
import www.mys.com.ourtalkandroid.fragment.MineFragment;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.NotificationUtils;
import www.mys.com.ourtalkandroid.utils.ToolUtils;
import www.mys.com.ourtalkandroid.utils.chat.ChatServiceUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.db.DBChatListUtils;

public class MainActivity extends BaseAppbarLoadingActivity {

    @ViewDesc(viewId = R.id.view_pager_content)
    public ViewPager viewPagerContent;
    @ViewDesc(viewId = R.id.bottom_navigation)
    public BottomNavigationBar bottomNavigationBar;
    private List<Fragment> fragmentList;
    private ChatFragment chatFragment;
    private BottomNavigationItem chatBottomNavigationItem;
    private TextBadgeItem chatBadgeItem;
    private ContactFragment contactFragment;
    private BottomNavigationItem contactBottomNavigationItem;
    private TextBadgeItem contactBadgeItem;
    private FindFragment findFragment;
    private BottomNavigationItem findBottomNavigationItem;
    private TextBadgeItem findBadgeItem;
    private MineFragment mineFragment;
    private BottomNavigationItem mineBottomNavigationItem;
    private TextBadgeItem mineBadgeItem;
    private MainAdapter mainAdapter;
    private DBChatListUtils dbChatListUtils;
    private int currentPotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationUtils.requestPermission(bottomNavigationBar);
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        String notificationType = getIntent().getStringExtra(StaticParam.NOTIFICATION_TYPE);
        if (notificationType != null) {
            int type = Integer.parseInt(notificationType);
            if (type > 0) {
                NotificationUtils.clearNotification(type);
            }
        }
        ConfigUtils.getUserInfo(getApplicationContext(), success -> {
            if (success) {
                ChatServiceUtils.getInstance().send(
                        ChatServiceUtils.START_CLIENT
                        , ConfigUtils.getUserInfo(getApplicationContext(), null).toString()
                );
            }
        });
        dbChatListUtils = new DBChatListUtils(getApplicationContext());
        fragmentList = new ArrayList<>();
        chatFragment = new ChatFragment();
        chatBottomNavigationItem = new BottomNavigationItem(R.drawable.ic_talks, R.string.talks);
        chatBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setAnimationDuration(200)
                .setBackgroundColorResource(R.color.red)
                .setHideOnSelect(false)
                .setText("0");
        chatBadgeItem.hide();
        chatBottomNavigationItem.setBadgeItem(chatBadgeItem);
        fragmentList.add(chatFragment);
        contactFragment = new ContactFragment();
        contactBottomNavigationItem = new BottomNavigationItem(R.drawable.ic_contacts, R.string.contacts);
        contactBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setAnimationDuration(200)
                .setBackgroundColorResource(R.color.red)
                .setHideOnSelect(false)
                .setText("0");
        contactBadgeItem.hide();
        contactBottomNavigationItem.setBadgeItem(contactBadgeItem);
        fragmentList.add(contactFragment);
        findFragment = new FindFragment();
        findBottomNavigationItem = new BottomNavigationItem(R.drawable.ic_find, R.string.find);
        findBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setAnimationDuration(200)
                .setBackgroundColorResource(R.color.red)
                .setHideOnSelect(false)
                .setText("0");
        findBadgeItem.hide();
        findBottomNavigationItem.setBadgeItem(findBadgeItem);
        fragmentList.add(findFragment);
        mineFragment = new MineFragment();
        mineBottomNavigationItem = new BottomNavigationItem(R.drawable.ic_talks, R.string.mine);
        mineBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setAnimationDuration(200)
                .setBackgroundColorResource(R.color.red)
                .setHideOnSelect(false)
                .setText("0");
        mineBottomNavigationItem.setBadgeItem(mineBadgeItem);
        mineBadgeItem.hide();
        fragmentList.add(mineFragment);
    }

    private void initView() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setInActiveColor(R.color.gray);
        bottomNavigationBar.setActiveColor(R.color.green);
        bottomNavigationBar.setBarBackgroundColor(R.color.black);
        bottomNavigationBar.addItem(chatBottomNavigationItem).setActiveColor(R.color.white);
        bottomNavigationBar.addItem(contactBottomNavigationItem).setActiveColor(R.color.white);
        bottomNavigationBar.addItem(findBottomNavigationItem).setActiveColor(R.color.white);
        bottomNavigationBar.addItem(mineBottomNavigationItem).setActiveColor(R.color.white);
        bottomNavigationBar.setFirstSelectedPosition(currentPotion).initialise();
        mainAdapter = new MainAdapter(getSupportFragmentManager(), fragmentList);
        viewPagerContent.setAdapter(mainAdapter);
    }

    private void initEvent() {
        viewPagerContent.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        currentPotion = position;
                        bottomNavigationBar.selectTab(position);
                        refreshState();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                currentPotion = position;
                viewPagerContent.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        ChatServiceUtils.getInstance().bindService(getApplicationContext()
                , new ChatCallBack.Stub() {
                    @Override
                    public void onBack(int type, String data) throws RemoteException {
                        LogUtils.log("service back type=" + type + ";data=" + data);
                        switch (type) {
                            case ChatServiceUtils.CONNECT_BACK:
                                runOnUiThread(() -> {
                                    updateServiceState();
                                });
                                break;
                            case ChatServiceUtils.STATE_BACK:
                                runOnUiThread(() -> {
                                    updateServiceState();
                                });
                                break;
                            case ChatServiceUtils.ADD_FRIENDS:
                                runOnUiThread(() -> {
                                    updateContact();
                                });
                                break;
                            case ChatServiceUtils.RECEIVE_MESSAGE:
                                runOnUiThread(() -> {
                                    updateTalk();
                                });
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshState();
    }

    private void refreshState() {
        switch (currentPotion) {
            case 1:
                appBarTitle.setText(R.string.contacts);
                break;
            case 2:
                appBarTitle.setText(R.string.find);
                break;
            case 3:
                appBarTitle.setText(R.string.mine);
                break;
            default:
                appBarTitle.setText(R.string.talks);
                break;
        }
        updateTalk();
        updateContact();
    }

    private void updateServiceState() {
        appBarLeftIcon.setImageResource(ToolUtils.getServiceState()
                ? R.drawable.left_point : R.drawable.right_point);
    }

    private void updateContact() {
        contactFragment.refreshNewFriendCount(success -> {
            if (success) {
                runOnUiThread(() -> {
                    int friendRequestCount = contactFragment.getUnReadCount();
                    if (friendRequestCount > 0) {
                        contactBadgeItem.setText(String.valueOf(friendRequestCount));
                        contactBadgeItem.show();
                    } else {
                        contactBadgeItem.hide();
                    }
                });
            }
        });
    }

    private void updateTalk() {
        chatFragment.onResult(dbChatListUtils.getDBChatLists());
        int unReadCount = chatFragment.getUnReadCount();
        if (unReadCount > 0) {
            chatBadgeItem.setText(String.valueOf(unReadCount));
            chatBadgeItem.show();
        } else {
            chatBadgeItem.hide();
        }
    }

    @Override
    public View.OnClickListener onRightIconClick2() {
        return view -> {
            try {
                startActivity(new Intent(MainActivity.this, QueryActivity.class));
            } catch (Exception e) {
                LogUtils.log("start activity error.e=" + e);
            }
        };
    }

    @Override
    public int getRightIcon() {
        return R.drawable.ic_add;
    }

    @Override
    public int getRightIcon2() {
        return R.drawable.ic_query;
    }

    @Override
    public String getAppBarTitle() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isMain() {
        return true;
    }

    @Override
    public int getLeftIcon() {
        return R.drawable.right_point;
    }

    @Override
    public View.OnClickListener onLeftIconClick() {
        return v -> {
            updateServiceState();
            ConfigUtils.getUserInfo(getApplicationContext()
                    , success -> {
                        if (success) {
                            ChatServiceUtils.getInstance().send(
                                    ChatServiceUtils.START_CLIENT
                                    , ConfigUtils.getUserInfo(getApplicationContext(), null).toString()
                            );
                        }
                    });
            Toast.makeText(getApplicationContext(), getString(R.string.start_connect)
                    , Toast.LENGTH_LONG).show();
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatServiceUtils.getInstance().unbindService(getApplicationContext());
    }

}
