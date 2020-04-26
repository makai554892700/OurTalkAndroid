package www.mys.com.ourtalkandroid.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import www.mys.com.ourtalkandroid.utils.db.DBOperateUtils;
import www.mys.com.ourtalkandroid.utils.db.sqllitehelper.OperateInfoSqlitHelper;

public class DatabaseProvider extends ContentProvider {

    public static final String AUTHORITY = "www.mys.com.ourtalkandroid";
    private static final int MATCH_CODE = 100;
    private static UriMatcher uriMatcher;
    public static final Uri NOTIFY_URI = Uri.parse("content://" + AUTHORITY + "/settings");
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "settings", MATCH_CODE);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        sqLiteDatabase = new DBOperateUtils(context).getDB();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection
            , @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = uriMatcher.match(uri);
        if (match == MATCH_CODE) {
            return sqLiteDatabase.query(OperateInfoSqlitHelper.TB_NAME, projection, selection
                    , selectionArgs, null, null, sortOrder, null);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) == MATCH_CODE) {
            sqLiteDatabase.insert(OperateInfoSqlitHelper.TB_NAME, null, values);
            notifyChange();
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == MATCH_CODE) {
            int delCount = sqLiteDatabase.delete(OperateInfoSqlitHelper.TB_NAME, selection, selectionArgs);
            notifyChange();
            return delCount;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection
            , @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == MATCH_CODE) {
            int updateCount = sqLiteDatabase.update(OperateInfoSqlitHelper.TB_NAME, values
                    , selection, selectionArgs);
            notifyChange();
            return updateCount;
        }
        return 0;
    }

    private void notifyChange() {
        context.getContentResolver().notifyChange(NOTIFY_URI, null);
    }

}