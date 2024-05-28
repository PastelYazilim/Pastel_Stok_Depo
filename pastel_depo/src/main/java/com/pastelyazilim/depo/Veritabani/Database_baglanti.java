package com.pastelyazilim.depo.Veritabani;

/**
 * Created on 18/02/17.
 */
import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_baglanti extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "sqllite_database";//database adı

    private static final String TABLE_NAME = "kayit";
    private static String sunucu = "sunucu";
    private static String kullaniciAdi = "kullaniciAdi";
    private static String id = "id";
    private static String veritabaniYolu = "veritabaniYolu";
    private static String port = "port";

    public Database_baglanti(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { 
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + kullaniciAdi + " TEXT,"
                + veritabaniYolu + " TEXT,"
                + port + " TEXT,"

                + sunucu + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }


    public void kayitEkle(String kullaniciAdi_, String veritabaniYolu_,String port_,String sunucu_) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(kullaniciAdi, kullaniciAdi_);
        values.put(veritabaniYolu, veritabaniYolu_);
        values.put(port, port_);
        values.put(sunucu, sunucu_);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public  ArrayList<HashMap<String, String>> kayitlar(){


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> kayitlist = new ArrayList<HashMap<String, String>>();
      if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                kayitlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return kayitlist;
    }

    public void kayitDuzenle(String kullaniciAdi_, String veritabaniYolu_,String port_,String sunucu_,int id_) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(kullaniciAdi, kullaniciAdi_);
        values.put(veritabaniYolu, veritabaniYolu_);
        values.put(port, port_);
        values.put(sunucu, sunucu_);


       db.update(TABLE_NAME, values, id + " = ?",
                new String[] { String.valueOf(id_) });
    }



    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}