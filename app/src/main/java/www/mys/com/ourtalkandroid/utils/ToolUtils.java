package www.mys.com.ourtalkandroid.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import www.mys.com.ourtalkandroid.base.StaticParam;

public class ToolUtils {

    public static ClipboardManager getClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static String copyText(ClipboardManager clipboard, String text) {
        ClipData clipData = ClipData.newPlainText(null, text);
        clipboard.setPrimaryClip(clipData);
        return getText(clipboard);
    }

    public static String getText(ClipboardManager clipboard) {
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            return clipData.getItemAt(0).getText().toString();
        }
        return null;
    }

    public static boolean getServiceState() {
        String tempStr = ResolverUtils.getInstance().getStringSetting(StaticParam.SERVICE_STATE);
        LogUtils.log("getServiceState=" + tempStr);
        return !StringUtils.isEmpty(tempStr) && "true".equals(tempStr);
    }

    public static boolean saveServiceState(boolean state) {
        ResolverUtils.getInstance().saveSetting(StaticParam.SERVICE_STATE, String.valueOf(state));
        return getServiceState();
    }

}
