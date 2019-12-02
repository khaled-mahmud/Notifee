package com.pgdit101.iit.notifee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductDBHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "ProductHelper.db";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_PRODUCTS = "products";

    //TABLE PRODUCTS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "id";

    //COLUMN product name
    public static final String KEY_PRODUCT_NAME = "product_name";

    //COLUMN description
    public static final String KEY_DESCRIPTION = "description";

    //COLUMN date
    public static final String KEY_DATE = "date";

    //SQL for creating products table
    public static final String SQL_TABLE_PRODUCTS = " CREATE TABLE " + TABLE_PRODUCTS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_PRODUCT_NAME + " TEXT, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_DATE + " INTEGER"
            + " ) ";


    public ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TABLE_PRODUCTS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }


    private long getDate(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = dateFormat.parse(day);
        } catch (ParseException e) {

        }
        return date.getTime();
    }


    public boolean insertProduct(String product, String description, String dateStr)
    {
        //Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_name", product);
        contentValues.put("description", description);
        contentValues.put("date", getDate(dateStr));
        db.insert(TABLE_PRODUCTS, null, contentValues);
        return true;
    }


    public boolean updateProduct(String id, String product, String description, String dateStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_name", product);
        contentValues.put("description", description);
        contentValues.put("date", getDate(dateStr));
        db.update(TABLE_PRODUCTS, contentValues, "id = ?", new String[]{id});
        return true;
    }


    public boolean deleteProduct(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, "id = ?", new String[]{id});
        return true;
    }




    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_PRODUCTS + " order by id desc", null);
        return result;
    }


    public Cursor getDataSpecific(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_PRODUCTS + " WHERE id = '" + id + "' order by id desc", null);
        return result;
    }


    public Cursor getDataToday()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_PRODUCTS +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', 'localtime') order by id desc", null);
        return result;
    }


    public Cursor getDataTomorrow()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery("select * from " + TABLE_PRODUCTS +
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime')  order by id desc", null);
        return result;
    }


    public Cursor getDataUpcoming()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery("select * from "+TABLE_PRODUCTS+
                " WHERE date(datetime(dateStr / 1000 , 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by id desc", null);
        return result;

    }
}
