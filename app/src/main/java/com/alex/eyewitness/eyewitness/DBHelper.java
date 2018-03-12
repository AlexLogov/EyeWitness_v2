package com.alex.eyewitness.eyewitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 10.02.2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sInstance;


    String CCORDINATES_TABLE_NAME = "Coordinatestable_4";
    String SETTINGS_TABLE_NAME = "settinga_table_4";
    private static SQLiteDatabase vDb ;


    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
            vDb = sInstance.getWritableDatabase();
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, "coordinatesDB_4.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // создаем таблицу с полями
        sqLiteDatabase.execSQL("create table "+CCORDINATES_TABLE_NAME+" ("
                + "id integer primary key autoincrement,"
                + "lat NUMERIC,"
                + "lng NUMERIC,"
                + "Accuracy NUMERIC,"
                + "type varchar(10),"
                + "inserted date"
                + ");");

        //создаем таблицу для хранения настроек
        sqLiteDatabase.execSQL("create table "+SETTINGS_TABLE_NAME+" ("
                + "id integer primary key autoincrement,"
                + "set_name varchar(100),"
                + "set_parent varchar(100),"
                + "value_string varchar(1000),"
                + "value_num numeric,"
                + "value_date date"
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Coordinates> getLastCoords(int pCountLastCoords){
        ArrayList vRetValues = new ArrayList();
        //SQLiteDatabase vDb = this.getReadableDatabase();

        //vDb.beginTransaction();
        Cursor c = //vDb.query(CCORDINATES_TABLE_NAME, new String[]{"*"}, null, null, null, null, null);
                vDb.rawQuery("SELECT lat ,lng , Accuracy ,type , inserted FROM "+CCORDINATES_TABLE_NAME+" order by id desc LIMIT  " + Integer.toString(pCountLastCoords) , new String[]{});
        DatabaseUtils.dumpCursor(c);
        Log.d("1", Integer.toString(c.getCount()));
        if (c.moveToFirst()){
            do {
                Coordinates vCoord = new Coordinates();
                vCoord.setLat(c.getDouble(c.getColumnIndex("lat")));
                vCoord.setLng(c.getDouble(c.getColumnIndex("lng")));
                vCoord.setAccuracy(c.getDouble(c.getColumnIndex("Accuracy")));
                vCoord.setType(c.getString(c.getColumnIndex("type")));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date vDate;
                try {
                    vDate = dateFormat.parse(c.getString(c.getColumnIndex("inserted")));
                } catch (ParseException e) {
                    vDate = null;
                }
                vCoord.setInserted(vDate);
                vRetValues.add(vCoord);
            } while(c.moveToNext());
        }
        c.close();
        //vDb.setTransactionSuccessful();
        //vDb.close();
        return vRetValues;
    }


    public void saveCoords(ContentValues pCv){
       // SQLiteDatabase vDb = this.getWritableDatabase();
        //vDb.beginTransaction();
        long rowID = vDb.insertOrThrow(CCORDINATES_TABLE_NAME, null, pCv);
        //vDb.setTransactionSuccessful();
        //vDb.close();
    }


    public boolean isHasVal(String pValueName){
        boolean iIsvalPresent = false;
        //SQLiteDatabase vDb = this.getWritableDatabase();
        //vDb.beginTransaction();
        Cursor c = vDb.rawQuery("SELECT count(1) FROM "+SETTINGS_TABLE_NAME+" where set_name = ? ", new String[] {pValueName});
        if (c.moveToFirst()){
            do {
                iIsvalPresent = true;
            } while(c.moveToNext());
        }
        c.close();
        //vDb.setTransactionSuccessful();
        //vDb.close();
        return iIsvalPresent;
    }

    public void setStringVal(String pValName, String pValue){
        //SQLiteDatabase vDb = this.getWritableDatabase();
        //vDb.beginTransaction();
        ContentValues vArgs = new ContentValues();
        vArgs.put("value_string", pValue);
        if (isHasVal( pValName)){
            //если значенеи настроек уже есть, обновим его
            vDb.update(SETTINGS_TABLE_NAME, vArgs, "set_name = ?", new String[] { pValName });
        }else{
          // если значения настроек нет, то вставми  :)
            vArgs.put("set_name", pValName);
            long rowID = vDb.insert(SETTINGS_TABLE_NAME, null, vArgs);
        }
        //vDb.setTransactionSuccessful();
        //vDb.close();
    }
    public void setNumVal(String pValName, Double pValue){
        //SQLiteDatabase vDb = this.getWritableDatabase();
        //vDb.beginTransaction();
        ContentValues vArgs = new ContentValues();
        vArgs.put("value_num", pValue);
        if (isHasVal( pValName)){
            //если значенеи настроек уже есть, обновим его
            vDb.update(SETTINGS_TABLE_NAME, vArgs, "set_name = ?", new String[] { pValName });
        }else{
            // если значения настроек нет, то вставми  :)
            vArgs.put("set_name", pValName);
            long rowID = vDb.insert(SETTINGS_TABLE_NAME, null, vArgs);
        }
        //vDb.setTransactionSuccessful();
        //vDb.close();
    }
    public String getStringVal (String pValName){
        //SQLiteDatabase vDb = this.getWritableDatabase();
        //vDb.beginTransaction();
        String vVal = null;
        Cursor c = vDb.rawQuery("SELECT value_string FROM "+SETTINGS_TABLE_NAME+" where set_name = ? ", new String[] {pValName});
        if (c.moveToFirst()){
            do {
                vVal = c.getString(c.getColumnIndex("value_string"));
            } while(c.moveToNext());
        }
        c.close();
        //vDb.setTransactionSuccessful();
        //vDb.close();
        return vVal;
    }
    public Double getNUmVal (String pValName){
        //SQLiteDatabase vDb = this.getReadableDatabase();
        Double vVal = null;
        Cursor c = vDb.rawQuery("SELECT value_string FROM "+SETTINGS_TABLE_NAME+" where set_name = ? ", new String[] {pValName});
        if (c.moveToFirst()){
            do {
                vVal = c.getDouble(c.getColumnIndex("value_num"));
            } while(c.moveToNext());
        }
        c.close();
        //vDb.setTransactionSuccessful();
        //vDb.close();
        return vVal;
    }
}
