package www.mys.com.ourtalkandroid.utils.db.sqllitehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import www.mys.com.ourtalkandroid.utils.db.model.DBOperateInfo;

public class OperateInfoSqlitHelper extends SQLiteOpenHelper {
    public static final String TB_NAME = "operateinfo";

    public OperateInfoSqlitHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + "(" +
                DBOperateInfo.ID + " integer primary key," +
                DBOperateInfo.DATA_KEY + " varchar," +
                DBOperateInfo.DATA + " varchar" +
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