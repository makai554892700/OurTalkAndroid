package www.mys.com.ourtalkandroid.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseActivity;
import www.mys.com.ourtalkandroid.utils.LogUtils;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            startActivity(new Intent(this, LoginActivity.class));
        } catch (Exception e) {
            LogUtils.log("start activity error.e=" + e);
        }
        finish();
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_first;
    }
}
