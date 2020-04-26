package www.mys.com.ourtalkandroid.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mayousheng.www.initview.ViewUtils;

import www.mys.com.ourtalkandroid.activity.LoginActivity;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.data.DataUtils;

public abstract class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (StaticParam.EXIT_APP.equals(intent.getAction())) {
                LogUtils.log("退出登录");
                ConfigUtils.logout();
                if (isMain()) {
                    Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);
                }
                finish();
            }
        }
    };

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticParam.EXIT_APP);
        registerReceiver(loginReceiver, filter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ViewUtils.initAllView(BaseActivity.class, this);
        if (isLogin()) {
            registerBroadcast();
        }
    }

    public abstract int getLayout();

    public boolean isLogin() {
        return true;
    }

    public boolean isMain() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLogin()) {
            unregisterReceiver(loginReceiver);
        }
    }
}
