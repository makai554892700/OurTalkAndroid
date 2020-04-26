package www.mys.com.ourtalkandroid.utils.db.sqllitehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import www.mys.com.ourtalkandroid.utils.db.model.DBTalkInfo;

public class TalkInfoSqlitHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "talk_info";

    public TalkInfoSqlitHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + "(" +
                DBTalkInfo.ID + " integer primary key," +
                DBTalkInfo.TYPE + " integer," +
                DBTalkInfo.TALK_TYPE + " integer," +
                DBTalkInfo.CHAT_KEY + " varchar," +
                DBTalkInfo.FROM_KEY + " varchar," +
                DBTalkInfo.TO_KEY + " varchar," +
                DBTalkInfo.AT_USERS + " varchar," +
                DBTalkInfo.DATA + " varchar," +
                DBTalkInfo.MSG_TIME + " integer" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
        try {
            db.execSQL("ALTER TABLE " +
                    TB_NAME + " CHANGE " +
                    oldColumn + " " + newColumn +
                    " " + typeColumn
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}