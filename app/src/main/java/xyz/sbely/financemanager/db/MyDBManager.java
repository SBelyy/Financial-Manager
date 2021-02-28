package xyz.sbely.financemanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import xyz.sbely.financemanager.layout.CategoryLayout;
import xyz.sbely.financemanager.model.Debt;
import xyz.sbely.financemanager.model.GraphPoint;
import xyz.sbely.financemanager.model.Report;

public class MyDBManager {
    private Context context;
    private MyDBHelper dbHelper;

    private SQLiteDatabase db;

    public MyDBManager(Context context) {
        this.context = context;
        dbHelper = new MyDBHelper(context);
    }

    public void openDb(){
        db = dbHelper.getWritableDatabase();
    }

    public void insertToDb(String type, String source, String date, String sum, String comment, int id_icon){
        ContentValues cv = new ContentValues();
        cv.put(MyDBConstants.TYPE, type);
        cv.put(MyDBConstants.SOURCE, source);
        cv.put(MyDBConstants.DATE, date);
        cv.put(MyDBConstants.SUM, sum);
        cv.put(MyDBConstants.COMMENT, comment);
        cv.put(MyDBConstants.ICON_ID, id_icon);
        db.insert(MyDBConstants.TABLE_NAME, null, cv);
    }

    public void insertToHistoryDb(String idn, String type, String date, String sum, String comment){
        ContentValues cv = new ContentValues();
        cv.put(MyDBConstants.IDN, idn);
        cv.put(MyDBConstants.TYPE, type);
        cv.put(MyDBConstants.DATE, date);
        cv.put(MyDBConstants.SUM, sum);
        cv.put(MyDBConstants.COMMENT, comment);
        db.insert(MyDBConstants.TABLE_2_NAME, null, cv);
    }

    public void insertToCategoryDb(String type, String name, int imageId) {
        ContentValues cv = new ContentValues();
        cv.put(MyDBConstants.TYPE, type);
        cv.put(MyDBConstants.NAME, name);
        cv.put(MyDBConstants.ICON_ID, imageId);
        db.insert(MyDBConstants.TABLE_3_NAME, null, cv);
    }

    public void updateDbSum (String id, String newSum) {
        ContentValues cv = new ContentValues();
        cv.put(MyDBConstants.SUM, newSum);
        db.update(MyDBConstants.TABLE_NAME, cv, "_id = ?", new String[]{id} );
    }

    public void deleteRow (String id) {
        db.delete(MyDBConstants.TABLE_NAME, MyDBConstants._ID + "=" + id, null);
        db.delete(MyDBConstants.TABLE_2_NAME, MyDBConstants.IDN + "=" + id, null);
    }

    public List<Debt> getFromDb(String category) {
        List<Debt> list = new ArrayList<>();

        try {
            Cursor cursor = db.query(MyDBConstants.TABLE_NAME, null, null, null,
                    null, null, null);

            if(cursor.moveToFirst())
                do{
                    String st = cursor.getString(cursor.getColumnIndex(MyDBConstants.TYPE));

                    if(st.equals(category)){
                        String name = cursor.getString(cursor.getColumnIndex(MyDBConstants.SOURCE));
                        String date = DateUtils.formatDateTime(context, Long.parseLong(cursor.getString(cursor.getColumnIndex(MyDBConstants.DATE))),
                                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME);
                        String sum = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                        String comment = cursor.getString(cursor.getColumnIndex(MyDBConstants.COMMENT));
                        String id = cursor.getString(cursor.getColumnIndex(MyDBConstants._ID));

                        list.add(new Debt(name, date, sum, comment, id));
                    }

                }while (cursor.moveToNext());

            cursor.close();
        } catch (Exception ignore){}

        return list;
    }

    public List<Report> getReportFromDb() {
        List<Report> list = new ArrayList<>();

        try {
            Cursor cursor = db.query(MyDBConstants.TABLE_NAME, null, null, null,
                    null, null, MyDBConstants.DATE + " DESC");

            if(cursor.moveToFirst())
                do{
                    String type = cursor.getString(cursor.getColumnIndex(MyDBConstants.TYPE));

                    if(!type.equals(MyDBConstants.I_GIVE) && !type.equals(MyDBConstants.GIVE_ME)){
                        String name = cursor.getString(cursor.getColumnIndex(MyDBConstants.SOURCE));
                        String date = cursor.getString(cursor.getColumnIndex(MyDBConstants.DATE));
                        String sum = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                        String comment = cursor.getString(cursor.getColumnIndex(MyDBConstants.COMMENT));
                        int id = cursor.getInt(cursor.getColumnIndex(MyDBConstants.ICON_ID));

                        list.add(new Report(type ,name, date, sum, comment, id));
                    }

                }while (cursor.moveToNext());

            cursor.close();
        } catch (Exception ignore){}

        return list;
    }

    public String getLastIdFromDb() {
        String id = "";

        Cursor cursor = db.query(MyDBConstants.TABLE_NAME, new String[]{MyDBConstants._ID}, null, null,
                null, null, null);

        if(cursor.moveToLast()){
            id += cursor.getInt(cursor.getColumnIndex(MyDBConstants._ID));
        }

        cursor.close();

        return id;
    }

    public synchronized ArrayList<CategoryLayout> getCategoryFromDb(String type) {
        ArrayList<CategoryLayout> newCategoryLayout = new ArrayList<>();

        Cursor cursor = db.query(MyDBConstants.TABLE_3_NAME, null, null, null,
                null, null, null);

        if(cursor.moveToFirst()){
            do {
                String st = cursor.getString(cursor.getColumnIndex(MyDBConstants.TYPE));

                if(st.equals(type)){
                    String name = cursor.getString(cursor.getColumnIndex(MyDBConstants.NAME));
                    int idIcon = cursor.getInt(cursor.getColumnIndex(MyDBConstants.ICON_ID));

                    CategoryLayout categoryLayout = new CategoryLayout(context, name, idIcon);
                    newCategoryLayout.add(categoryLayout);
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        return newCategoryLayout;
    }

    public synchronized List<GraphPoint> getPointForType(String type){
        List<GraphPoint> points = new ArrayList<>();

        Cursor cursor = db.query(MyDBConstants.TABLE_NAME, new String[]{MyDBConstants.TYPE, MyDBConstants.SUM, MyDBConstants.DATE}, null,
                null, null, null, MyDBConstants.DATE);

        if(cursor.moveToFirst())
            do{
                String st = cursor.getString(cursor.getColumnIndex(MyDBConstants.TYPE));

                if(st.equals(type)){
                    float sum = Float.parseFloat(cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM)));
                    long date = Long.parseLong(cursor.getString(cursor.getColumnIndex(MyDBConstants.DATE)));
                    int dateInDay = (int) (date / (24 * 60 * 60 * 1000));
                    points.add(new GraphPoint(sum, dateInDay));
                }
            }while (cursor.moveToNext());

        cursor.close();

        return points;
    }

    public String[] getSumFromDb(){
        BigDecimal sum = new BigDecimal("0");
        BigDecimal sumGiveMe = new BigDecimal("0");
        BigDecimal sumIGive = new BigDecimal("0");
        int numGiveMe = 0;
        int numIGive = 0;

        Cursor cursor = db.query(MyDBConstants.TABLE_NAME, new String[]{MyDBConstants.TYPE, MyDBConstants.SUM}, null,
                null, null, null, null);

        if(cursor.moveToFirst())
        do{
            String st = cursor.getString(cursor.getColumnIndex(MyDBConstants.TYPE));

            if(st.equals(MyDBConstants.INCOME)){
                String num = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                sum = sum.add(BigDecimal.valueOf(Double.parseDouble(num)));
            } else if (st.equals(MyDBConstants.EXPENSE)){
                String num = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                sum = sum.subtract(BigDecimal.valueOf(Double.parseDouble(num)));
            } else if(st.equals(MyDBConstants.GIVE_ME)){
                String num = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                sumGiveMe = sumGiveMe.add(BigDecimal.valueOf(Double.parseDouble(num)));
                numGiveMe++;
            } else if (st.equals(MyDBConstants.I_GIVE)){
                String num = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                sumIGive = sumIGive.add(BigDecimal.valueOf(Double.parseDouble(num)));
                numIGive++;
            }
        }while (cursor.moveToNext());

        cursor.close();

        String sum1 = sum + "";
        String sum2 = sumGiveMe + "";
        String sum3 = sumIGive + "";
        String num1 = numGiveMe + "";
        String num2 = numIGive + "";
        return new String[]{sum1, sum2, sum3, num1, num2};
    }

    public synchronized String getTotalSumFromDb(String source) {
        BigDecimal sum = new BigDecimal("0");

        Cursor cursor = db.query(MyDBConstants.TABLE_NAME, new String[]{MyDBConstants.SOURCE, MyDBConstants.SUM}, null,
                null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                String tableType = cursor.getString(cursor.getColumnIndex(MyDBConstants.SOURCE));

                if(tableType.equals(source)) {
                    String number = cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM));
                    sum = sum.add(BigDecimal.valueOf(Double.parseDouble(number)));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return sum + "";
    }

    public List<String> getHistoryFromDb(String id) {
        List<String> list = new ArrayList<>();

        try {
            Cursor cursor = db.query(MyDBConstants.TABLE_2_NAME, null, null, null,
                    null, null, null);

            if(cursor.moveToFirst())
                do{
                    String st = cursor.getString(cursor.getColumnIndex(MyDBConstants.IDN));

                    if(st.equals(id)){
                        list.add(cursor.getString(cursor.getColumnIndex(MyDBConstants.TYPE)));
                        list.add(cursor.getString(cursor.getColumnIndex(MyDBConstants.SUM)));
                        list.add(cursor.getString(cursor.getColumnIndex(MyDBConstants.COMMENT)));

                        String date = DateUtils.formatDateTime(context, Long.parseLong(cursor.getString(cursor.getColumnIndex(MyDBConstants.DATE))),
                                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME);
                        list.add(date);
                    }

                }while (cursor.moveToNext());

            cursor.close();
        } catch (Exception ignore){}

        return list;
    }

    public void closeDb(){
        dbHelper.close();
    }
}
