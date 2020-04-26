package www.mys.com.ourtalkandroid.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

import www.mys.com.ourtalkandroid.utils.db.model.DBChatList;
import www.mys.com.ourtalkandroid.utils.db.sqllitehelper.ChatListSqlitHelper;


public class DBChatListUtils {
    // 数据库名称
    private static String DB_NAME = "chat_list.db";
    // 数据库版本
    private static int DB_VERSION = 1;
    private SQLiteDatabase db;
    private ChatListSqlitHelper dbHelper;

    public DBChatListUtils(Context context) {
        dbHelper = new ChatListSqlitHelper(context.getApplicationContext(), DB_NAME
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

    public ArrayList<DBChatList> getDBChatLists() {
        ArrayList<DBChatList> result = new ArrayList<>();
        Cursor cursor = db.query(ChatListSqlitHelper.TB_NAME, null
                , null, null, null
                , null, DBChatList.MSG_TIME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(getDBChatList(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public DBChatList getDBChatListByKey(String key) {
        Cursor cursor = db.query(ChatListSqlitHelper.TB_NAME, null
                , DBChatList.CHAT_KEY + "=?", new String[]{key}, null
                , null, null);
        DBChatList result;
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = getDBChatList(cursor);
        } else {
            result = null;
        }
        cursor.close();
        return result;
    }

    public Long saveDBChatList(DBChatList dbChatList) {
        if (dbChatList == null || TextUtils.isEmpty(dbChatList.chatKey)) {
            return -1L;
        }
        DBChatList tempDBChatList = getDBChatListByKey(dbChatList.chatKey);
        if (tempDBChatList == null) {
            ContentValues values = new ContentValues();
            if (dbChatList.id > 0) {
                values.put(DBChatList.ID, dbChatList.id);
            }
            values.put(DBChatList.TYPE, dbChatList.type);
            values.put(DBChatList.UNREAD_COUNT, dbChatList.unReadCount);
            values.put(DBChatList.CHAT_KEY, dbChatList.chatKey);
            values.put(DBChatList.IMG_URL, dbChatList.imgUrl);
            values.put(DBChatList.USER_NAME, dbChatList.userName);
            values.put(DBChatList.MSG_TITLE, dbChatList.msgTitle);
            values.put(DBChatList.MSG_DESC, dbChatList.msgDesc);
            values.put(DBChatList.MSG_TIME, dbChatList.msgTime);
            return db.insert(ChatListSqlitHelper.TB_NAME, DBChatList.ID, values);
        } else {
            ContentValues values = new ContentValues();
            values.put(DBChatList.UNREAD_COUNT, dbChatList.unReadCount);
            values.put(DBChatList.IMG_URL, dbChatList.imgUrl);
            values.put(DBChatList.MSG_TITLE, dbChatList.msgTitle);
            values.put(DBChatList.MSG_DESC, dbChatList.msgDesc);
            values.put(DBChatList.MSG_TIME, dbChatList.msgTime);
            return (long) db.update(ChatListSqlitHelper.TB_NAME, values
                    , DBChatList.ID + "=?"
                    , new String[]{String.valueOf(tempDBChatList.id)});
        }
    }

    public int delDBChatList(int id) {
        return db.delete(ChatListSqlitHelper.TB_NAME,
                DBChatList.ID + "=?", new String[]{String.valueOf(id)});
    }

    public int clearDBChatList() {
        return db.delete(ChatListSqlitHelper.TB_NAME, null, null);
    }

    private DBChatList getDBChatList(Cursor cursor) {
        DBChatList result = new DBChatList(null);
        result.id = cursor.getInt(0);
        result.type = cursor.getInt(1);
        result.unReadCount = cursor.getInt(2);
        result.chatKey = cursor.getString(3);
        result.imgUrl = cursor.getString(4);
        result.userName = cursor.getString(5);
        result.msgTitle = cursor.getString(6);
        result.msgDesc = cursor.getString(7);
        result.msgTime = cursor.getLong(8);
        return result;
    }

}
