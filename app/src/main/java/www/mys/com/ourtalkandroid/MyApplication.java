package www.mys.com.ourtalkandroid;

import android.app.Application;
import android.content.Intent;

import www.mys.com.ourtalkandroid.service.ChatService;
import www.mys.com.ourtalkandroid.service.MyService;
import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.NotificationUtils;
import www.mys.com.ourtalkandroid.utils.ResolverUtils;
import www.mys.com.ourtalkandroid.utils.ToolUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ResolverUtils.init(getContentResolver());
        NotificationUtils.init(getApplicationContext());
        ToolUtils.saveServiceState(false);
        try {
            startService(new Intent(getApplicationContext(), MyService.class));
        } catch (Exception e) {
            LogUtils.log("start service error.e=" + e);
        }
        try {
            startService(new Intent(this, ChatService.class));
        } catch (Exception e) {
            LogUtils.log("start service error.e=" + e);
        }
    }
}
