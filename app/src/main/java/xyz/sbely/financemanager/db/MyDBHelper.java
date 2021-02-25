package xyz.sbely.financemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, MyDBConstants.DB_NAME, null, MyDBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyDBConstants.TABLE_STRUCTURE);
        db.execSQL(MyDBConstants.TABLE_2_STRUCTURE);
        db.execSQL(MyDBConstants.TABLE_3_STRUCTURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyDBConstants.DROP_TABLE);
        db.execSQL(MyDBConstants.DROP_2_TABLE);
        db.execSQL(MyDBConstants.DROP_3_TABLE);
        onCreate(db);
    }
}
