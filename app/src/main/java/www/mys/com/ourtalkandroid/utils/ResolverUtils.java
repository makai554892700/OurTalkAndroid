package www.mys.com.ourtalkandroid.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.mayousheng.www.basepojo.BasePoJo;

import www.mys.com.ourtalkandroid.base.StaticParam;
import www.mys.com.ourtalkandroid.provider.DatabaseProvider;
import www.mys.com.ourtalkandroid.utils.db.model.DBOperateInfo;

public class ResolverUtils {

    private static ResolverUtils resolverUtils = new ResolverUtils();
    private static ContentResolver contentResolver;

    public String getStringSetting(String key) {
        if (contentResolver == null) {
            LogUtils.log("getOperateData error.contentResolver is null.key=" + key);
            return null;
        }
        Cursor cursor = contentResolver.query(DatabaseProvider.NOTIFY_URI, null
                , DBOperateInfo.DATA_KEY + "=?", new String[]{key}, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        String resultStr = null;
        if (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            resultStr = cursor.getString(2);
        }
        cursor.close();
        return resultStr;
    }

    public String saveSetting(String key, String value) {
        if (contentResolver == null) {
            LogUtils.log("getOperateData error.contentResolver is null.key=" + key);
            return null;
        }
        String tempT = getStringSetting(key);
        ContentValues values = new ContentValues();
        values.put(DBOperateInfo.DATA_KEY, key);
        values.put(DBOperateInfo.DATA, value);
        if (tempT == null) {
            LogUtils.log("tempT is null");
            contentResolver.insert(DatabaseProvider.NOTIFY_URI, values);
        } else {
            contentResolver.update(DatabaseProvider.NOTIFY_URI, values
                    , DBOperateInfo.DATA_KEY + "=?", new String[]{key});
        }
        return getStringSetting(key);
    }

    public <T extends BasePoJo> T saveOperateData(String key, T t, Class<T> clazz) {
        saveSetting(key, t == null ? StaticParam.EMPTY : t.toString());
        return getOperateData(key, clazz);
    }

    public <T extends BasePoJo> T getOperateData(String key, Class<T> t) {
        return BasePoJo.strToObject(t, getStringSetting(key));
    }

    public static ResolverUtils getInstance() {
        return resolverUtils;
    }

    private ResolverUtils() {
    }

    public static void init(ContentResolver contentResolver) {
        if (ResolverUtils.contentResolver == null) {
            ResolverUtils.contentResolver = contentResolver;
        }
    }
}
