package www.mys.com.ourtalkandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseAppbarLoadingActivity;
import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.data.DataUtils;

public class SettingActivity extends BaseAppbarLoadingActivity {

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
    @ViewDesc(viewId = R.id.recycler_view)
    public RecyclerView recyclerView;
    @ViewDesc(viewId = R.id.change_account)
    public TextView changeAccount;
    @ViewDesc(viewId = R.id.logout)
    public TextView logout;
    private String titleStr;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String data = intent.getStringExtra(StaticParam.DATA);
        titleStr = data;
        super.onCreate(savedInstanceState);
        user = ConfigUtils.getUserInfo(getApplicationContext()
                , success -> {
                    if (success) {
                        user = ConfigUtils.getUserInfo(getApplicationContext(), null);
                        initView();
                    }
                });
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.log("user=" + user);
    }

    private void initView() {
        itemTitle.setText(user.nickName);
        itemDesc.setText(
                getString(R.string.nick_name, user.nickName)
                        + "\n"
                        + getString(R.string.user_name, user.userName)
        );
    }

    private void initEvent() {
        changeAccount.setOnClickListener(v -> {
        });
        logout.setOnClickListener(v -> {
            isLoading(true);
            DataUtils.logout(getApplicationContext()
                    , new DataUtils.StringBack() {
                        @Override
                        public void onSuccess(String response) {
                            isLoading(false);
                            ConfigUtils.logout();
                            Intent intent = new Intent();
                            intent.setAction(StaticParam.EXIT_APP);
                            getApplicationContext().sendBroadcast(intent);
                        }

                        @Override
                        public void onFailed(Integer status, String message) {
                            isLoading(false);
                        }
                    });
        });
    }

    @Override
    public String getAppBarTitle() {
        return titleStr;
    }

    @Override
    public int getLeftIcon() {
        return R.drawable.ic_back;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }
}
