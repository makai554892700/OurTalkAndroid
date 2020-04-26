package www.mys.com.ourtalkandroid.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.json.JSONArray;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.utils.LogUtils;
import www.mys.com.ourtalkandroid.utils.db.model.DBTalkInfo;
import www.mys.com.ourtalkandroid.utils.db.sqllitehelper.TalkInfoSqlitHelper;


public class DBTalkInfoUtils {
    // 数据库名称
    private static String DB_NAME = "tal_info.db";
    // 数据库版本
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private TalkInfoSqlitHelper dbHelper;

    public DBTalkInfoUtils(Context context) {
        dbHelper = new TalkInfoSqlitHelper(context.getApplicationContext(), DB_NAME
                , null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDB() {
        return db;
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public DBTalkInfo getDBTalkInfoById(int id) {
        DBTalkInfo result = null;
        Cursor cursor = db.query(TalkInfoSqlitHelper.TB_NAME, null
                , DBTalkInfo.ID + "=?", new String[]{String.valueOf(id)}
                , null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            result = getDBTalkInfo(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public ArrayList<DBTalkInfo> getDBTalkInfoByKey(String key, int page, int count) {
        ArrayList<DBTalkInfo> result = new ArrayList<>();
        Cursor cursor = db.query(TalkInfoSqlitHelper.TB_NAME, null
                , DBTalkInfo.CHAT_KEY + "=?", new String[]{key}, null
                , null, DBTalkInfo.MSG_TIME + " desc ", page + "," + count);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(getDBTalkInfo(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public ArrayList<DBTalkInfo> getDBTalkInfoByKey(String key) {
        ArrayList<DBTalkInfo> result = new ArrayList<>();
        Cursor cursor = db.query(TalkInfoSqlitHelper.TB_NAME, null
                , DBTalkInfo.CHAT_KEY + "=?", new String[]{key}, null
                , null, DBTalkInfo.MSG_TIME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(getDBTalkInfo(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public Long saveDBTalkInfo(DBTalkInfo dbTalkInfo) {
        if (dbTalkInfo == null || TextUtils.isEmpty(dbTalkInfo.chatKey)) {
            return -1L;
        }
        ContentValues values = new ContentValues();
        if (dbTalkInfo.id > 0) {
            values.put(DBTalkInfo.ID, dbTalkInfo.id);
        }
        if (dbTalkInfo.atUsers == null) {
            dbTalkInfo.atUsers = new ArrayList<>();
        }
        values.put(DBTalkInfo.TYPE, dbTalkInfo.type);
        values.put(DBTalkInfo.TALK_TYPE, dbTalkInfo.talkType);
        values.put(DBTalkInfo.CHAT_KEY, dbTalkInfo.chatKey);
        values.put(DBTalkInfo.FROM_KEY, dbTalkInfo.fromKey);
        values.put(DBTalkInfo.TO_KEY, dbTalkInfo.toKey);
        values.put(DBTalkInfo.AT_USERS, dbTalkInfo.atUsers.toString());
        values.put(DBTalkInfo.DATA, dbTalkInfo.data);
        values.put(DBTalkInfo.MSG_TIME, dbTalkInfo.msgTime);
        return db.insert(TalkInfoSqlitHelper.TB_NAME, DBTalkInfo.ID, values);
    }

    public int delDBTalkInfos(String key) {
        return db.delete(TalkInfoSqlitHelper.TB_NAME,
                DBTalkInfo.CHAT_KEY + "=?", new String[]{key});
    }

    public int delDBTalkInfo(int id) {
        return db.delete(TalkInfoSqlitHelper.TB_NAME,
                DBTalkInfo.ID + "=?", new String[]{String.valueOf(id)});
    }

    private DBTalkInfo getDBTalkInfo(Cursor cursor) {
        DBTalkInfo result = new DBTalkInfo(null);
        result.id = cursor.getInt(0);
        result.type = cursor.getInt(1);
        result.talkType = cursor.getInt(2);
        result.chatKey = cursor.getString(3);
        result.fromKey = cursor.getString(4);
        result.toKey = cursor.getString(5);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(cursor.getString(6));
        } catch (Exception e) {
            LogUtils.log("jsonArray parse error.e=" + e);
        }
        if (jsonArray != null) {
            ArrayList<String> atUsers = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    atUsers.add(jsonArray.getString(i));
                } catch (Exception e) {
                }
            }
            result.atUsers = atUsers;
        }
        result.data = cursor.getString(7);
        result.msgTime = cursor.getLong(8);
        return result;
    }

}
