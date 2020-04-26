package www.mys.com.ourtalkandroid.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mayousheng.www.initview.ViewDesc;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.BaseLoadingActivity;
import www.mys.com.ourtalkandroid.pojo.User;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.StringUtils;
import www.mys.com.ourtalkandroid.utils.data.ConfigUtils;
import www.mys.com.ourtalkandroid.utils.data.DataUtils;

public class LoginActivity extends BaseLoadingActivity {

    @ViewDesc(viewId = R.id.register)
    public TextView register;
    @ViewDesc(viewId = R.id.login)
    public TextView login;
    @ViewDesc(viewId = R.id.user_name)
    public EditText userName;
    @ViewDesc(viewId = R.id.pass)
    public EditText pass;
    @ViewDesc(viewId = R.id.submit)
    public Button submit;
    private boolean isLogin = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConfigUtils.checkLogin(this)) {
            return;
        }
        initEvent();
        refreshView();
    }

    private void initEvent() {
        userName.setText("admin");
        pass.setText("admin123");
        submit.setOnClickListener(v -> {
            if (!checkEdit(userName, 1, 32)) {
                return;
            }
            if (!checkEdit(pass, 6, 32)) {
                return;
            }
            login();
        });
        register.setOnClickListener(v -> {
            isLogin = false;
            refreshView();
        });
        login.setOnClickListener(v -> {
            isLogin = true;
            refreshView();
        });
    }

    private void login() {
        isLoading(true);
        if (isLogin) {
            DataUtils.login(getApplicationContext(), userName.getText().toString()
                    , pass.getText().toString()
                    , new DataUtils.BodyBack<User>() {
                        @Override
                        public void onSuccess(User response) {
                            isLoading(false);
                            LogUtils.log("onStrSuccess" + response);
                            ConfigUtils.checkLogin(LoginActivity.this);
                        }

                        @Override
                        public void onFailed(Integer status, final String message) {
                            isLoading(false);
                            LogUtils.log("onFailed" + status + ";message=" + message);
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
        } else {
            DataUtils.register(getApplicationContext(), userName.getText().toString()
                    , pass.getText().toString()
                    , new DataUtils.BodyBack<User>() {
                        @Override
                        public void onSuccess(User response) {
                            isLoading(false);
                            LogUtils.log("onStrSuccess" + response);
                            ConfigUtils.checkLogin(LoginActivity.this);
                        }

                        @Override
                        public void onFailed(Integer status, final String message) {
                            isLoading(false);
                            LogUtils.log("onFailed" + status + ";message=" + message);
                            runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
        }
    }

    private void refreshView() {
        if (isLogin) {
            login.setBackgroundResource(R.drawable.button_right_check);
            register.setBackgroundResource(R.drawable.button_left);
            submit.setText(R.string.login);
        } else {
            login.setBackgroundResource(R.drawable.button_right);
            register.setBackgroundResource(R.drawable.button_left_check);
            submit.setText(R.string.register);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    private boolean checkEdit(EditText editText, int min, int max) {
        Editable editable = editText.getText();
        if (editable == null || StringUtils.isEmpty(editable.toString())) {
            Toast.makeText(this, getString(R.string.edit_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        String data = editable.toString();
        if (data.length() < min || data.length() > max) {
            Toast.makeText(this, getString(R.string.edit_len_error, min, max), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean isLogin() {
        return false;
    }
}
