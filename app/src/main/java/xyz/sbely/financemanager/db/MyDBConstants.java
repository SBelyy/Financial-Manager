package xyz.sbely.financemanager.db;

import android.provider.BaseColumns;

public final class MyDBConstants implements BaseColumns {
    private MyDBConstants() {
    }

    static final String DB_NAME = "finance.db";
    static final int DB_VERSION = 5;
    static final String _ID = "_id";

    static final String TABLE_NAME = "expenseincome";
    static final String TYPE = "type";
    static final String SOURCE = "source";
    static final String DATE = "date";
    static final String SUM = "sum";
    static final String COMMENT = "comment";
    static final String ICON_ID = "icon";
    static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + TYPE + " TEXT," +
            SOURCE + " TEXT," + DATE + " TEXT," + SUM + " TEXT," + COMMENT + " TEXT," + ICON_ID + " INTEGER)";
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    static final String TABLE_2_NAME = "historydebt";
    static final String IDN = "idn";
    static final String TABLE_2_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_2_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + IDN + " TEXT,"
            + TYPE + " TEXT," + DATE + " TEXT," + SUM + " TEXT," + COMMENT + " TEXT)";
    static final String DROP_2_TABLE = "DROP TABLE IF EXISTS " + TABLE_2_NAME;

    static final String TABLE_3_NAME = "category";
    static final String NAME = "name";
    static final String TABLE_3_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_3_NAME + " (" + _ID + " INTEGER PRIMARY KEY," +
            TYPE + " TEXT," + NAME + " TEXT," + ICON_ID + " INTEGER)";
    static final String DROP_3_TABLE = "DROP TABLE IF EXISTS " + TABLE_3_NAME;

    public static final String REFILL = "refill";
    public static final String REDUCTION = "reduction";
    public static final String CREATE_DEBT = "create";

    public static final String I_GIVE = "igive";
    public static final String GIVE_ME = "giveme";

    public static final String EXPENSE = "expense";
    public static final String INCOME = "income";
}
