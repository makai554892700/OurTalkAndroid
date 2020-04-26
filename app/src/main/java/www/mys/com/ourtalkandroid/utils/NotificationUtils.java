package www.mys.com.ourtalkandroid.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.snackbar.Snackbar;

import www.mys.com.ourtalkandroid.R;
import www.mys.com.ourtalkandroid.base.StaticParam;

public class NotificationUtils {

    private static NotificationManagerCompat mNotificationManagerCompat;
    private static Context context;

    public static void init(Context context) {
        if (NotificationUtils.context == null) {
            NotificationUtils.context = context;
        }
        if (mNotificationManagerCompat == null) {
            mNotificationManagerCompat = NotificationManagerCompat.from(context);
        }
    }

    public static void clearNotification(int id) {
        mNotificationManagerCompat.cancel(id);
    }

    public static void shoNotification(Class<? extends Activity> clazz, int id, String from, String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    context.getPackageName(), context.getResources().getString(R.string.app_name)
                    , importance);
            channel.setDescription(context.getPackageName());
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        Intent intent = new Intent(context, clazz);
        intent.putExtra(StaticParam.NOTIFICATION_TYPE, String.valueOf(id));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getPackageName())
                .setSmallIcon(R.drawable.ic_head_1)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_talks_black))
                .setContentTitle(from)
                .setContentText(data)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(data))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        mNotificationManagerCompat.notify(id, builder.build());
    }

    public static void requestPermission(View rootView) {
        boolean areNotificationsEnabled = mNotificationManagerCompat.areNotificationsEnabled();
        if (!areNotificationsEnabled) {
            Snackbar snackbar = Snackbar
                    .make(rootView, context.getString(R.string.request_notification),
                            Snackbar.LENGTH_LONG)
                    .setAction(context.getString(R.string.sure), (view) -> {
                        openNotificationSettingsForApp(context);
                    });
            snackbar.show();
        }
    }

    public static void openNotificationSettingsForApp(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", context.getPackageName());
        intent.putExtra("app_uid", context.getApplicationInfo().uid);
        context.startActivity(intent);
    }

    public static final class Type {
        public static final int MESSAGE = 1;
        public static final int FRIEND_REQUEST = 2;
    }

}
