package www.mys.com.ourtalkandroid.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.utils.db.model.DBOperateInfo;
import www.mys.com.ourtalkandroid.utils.db.sqllitehelper.OperateInfoSqlitHelper;


public class DBOperateUtils {
    // 数据库名称
    private static String DB_NAME = "operateinfo.db";
    // 数据库版本
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private OperateInfoSqlitHelper dbHelper;

    public DBOperateUtils(Context context) {
        dbHelper = new OperateInfoSqlitHelper(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDB() {
        return db;
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public ArrayList<DBOperateInfo> getDBOperateInfos() {
        ArrayList<DBOperateInfo> dbOperateInfos = new ArrayList<DBOperateInfo>();
        Cursor cursor = db.query(OperateInfoSqlitHelper.TB_NAME, null, null, null, null,
                null, DBOperateInfo.ID + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            DBOperateInfo dbOperateInfo = new DBOperateInfo(null);
            dbOperateInfo.id = Integer.parseInt(cursor.getString(0));
            dbOperateInfo.data_key = cursor.getString(1);
            dbOperateInfo.data = cursor.getString(2);
            dbOperateInfos.add(dbOperateInfo);
            cursor.moveToNext();
        }
        cursor.close();
        return dbOperateInfos;
    }

    public DBOperateInfo getDBOperateInfoByKey(String key) {
        DBOperateInfo result = null;
        Cursor cursor = db.query(OperateInfoSqlitHelper.TB_NAME, null, DBOperateInfo.DATA_KEY + "=?",
                new String[]{key}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            result = new DBOperateInfo(null);
            result.id = cursor.getInt(0);
            result.data_key = key;
            result.data = cursor.getString(2);
            break;
        }
        cursor.close();
        return result;
    }


    public Boolean haveDBOperateInfo(int dbSyncInfo) {
        Boolean b = false;
        Cursor cursor = db.query(OperateInfoSqlitHelper.TB_NAME, null, DBOperateInfo.ID
                + "=?", new String[]{String.valueOf(dbSyncInfo)}, null, null, null);
        b = cursor.moveToFirst();
        cursor.close();
        return b;
    }

    public int updateDBOperateInfo(DBOperateInfo dbOperateInfo) {
        int id = -1;
        if (dbOperateInfo != null && !TextUtils.isEmpty(dbOperateInfo.data_key)) {
            id = dbOperateInfo.id;
            if (haveDBOperateInfo(id)) {
                ContentValues values = new ContentValues();
                values.put(DBOperateInfo.DATA_KEY, dbOperateInfo.data_key);
                values.put(DBOperateInfo.DATA, dbOperateInfo.data);
                id = db.update(OperateInfoSqlitHelper.TB_NAME, values, DBOperateInfo.ID + "="
                        + dbOperateInfo.id, null);
            }
        }
        return id;
    }

    public Long saveDBOperateInfo(DBOperateInfo dbOperateInfo) {
        if (dbOperateInfo == null || TextUtils.isEmpty(dbOperateInfo.data_key)) {
            return -1L;
        }
        ContentValues values = new ContentValues();
        if (dbOperateInfo.id > 0) {
            values.put(DBOperateInfo.ID, dbOperateInfo.id);
        }
        values.put(DBOperateInfo.DATA_KEY, dbOperateInfo.data_key);
        values.put(DBOperateInfo.DATA, dbOperateInfo.data);
        return db.insert(OperateInfoSqlitHelper.TB_NAME, DBOperateInfo.ID, values);
    }

    public int delDBOperateInfo(String key) {
        DBOperateInfo dbOperateInfo = getDBOperateInfoByKey(key);
        if (dbOperateInfo == null) {
            return -1;
        }
        return delDBOperateInfo(dbOperateInfo.id);
    }

    public int delDBOperateInfo(int dbSyncInfoId) {
        int id = -1;
        if (haveDBOperateInfo(dbSyncInfoId)) {
            id = db.delete(OperateInfoSqlitHelper.TB_NAME,
                    DBOperateInfo.ID + "=?", new String[]{String.valueOf(dbSyncInfoId)});
        }
        return id;
    }
}
