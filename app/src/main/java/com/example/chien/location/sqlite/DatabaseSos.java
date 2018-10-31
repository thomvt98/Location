package com.example.chien.location.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chien.location.model.GisTable;
import com.example.chien.location.model.SosInfo;
import com.example.chien.location.model.SosMedia;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSos extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteSos";

    private static final String DATABASE_NAME = "sos.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "SOS";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_TITLE = "TITLE";
    private static final String COLUMN_NOTE = "NOTE";
    private static final String COLUMN_PRIORITY = "PRIORITY";
    private static final String COLUMN_SOSCODE = "CODE";
    private static final String COLUMN_CREATEDTIME = "CREATEDTIME";
    private static final String COLUMN_GISDATA = "GISDATA";
    private static final String COLUMN_CREATEDBY = "CREATEDBY";
//
    //table SOS_MEDIA
    private static final String TABLE_SOS_MEDIA = "SOS_MEDIA";
    private static final String COLUMN_ID_SOS_MEDIA = "ID_SOS";
    private static final String COLUMN_FILEPATH = "FILEPATH";


    public DatabaseSos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_NOTE + " TEXT NOT NULL, "
                + COLUMN_PRIORITY + " TEXT NOT NULL, "
                + COLUMN_SOSCODE + " TEXT NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL,"
                + COLUMN_CREATEDTIME + " TEXT NOT NULL,"
                + COLUMN_GISDATA + " TEXT NOT NULL,"
                + COLUMN_CREATEDBY + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(sql);
        String sqlSosmedia="CREATE TABLE IF NOT EXISTS " + TABLE_SOS_MEDIA + "("
                + COLUMN_ID_SOS_MEDIA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_SOSCODE + " TEXT NOT NULL, "
                + COLUMN_FILEPATH + " TEXT NOT NULL) ";
        sqLiteDatabase.execSQL(sqlSosmedia);
//
//                + COLUMN_GISDATA + " TEXT NOT NULL,"
//                + COLUMN_CREATEDBY + " TEXT NOT NULL, "
//                + COLUMN_CREATEDTIME + " TEXT NOT NULL,"
//                + COLUMN_PRIORITY + " TEXT NOT NULL)";

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SOS_MEDIA);
        onCreate(sqLiteDatabase);
    }
    public SosMedia getMedia(String soscode) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SOS_MEDIA, new String[]{COLUMN_ID_SOS_MEDIA,
                        COLUMN_SOSCODE,
                        COLUMN_FILEPATH}, COLUMN_SOSCODE + "=?",
                new String[]{soscode}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SosMedia sos = new SosMedia(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2));
        // return note
        return sos;
    }
    public void addSOSMedia(SosMedia sosMedia)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOSCODE,sosMedia.getSos_code());
        values.put(COLUMN_FILEPATH,sosMedia.getFile_path());
        db.insert(TABLE_SOS_MEDIA,null,values);
        db.close();
    }
    public void addData(SosInfo sos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SOSCODE, sos.getSos_code());
        values.put(COLUMN_NOTE, sos.getSos_note());
        values.put(COLUMN_GISDATA, sos.getGisdata());
        values.put(COLUMN_TITLE, sos.getSos_title());
        values.put(COLUMN_PRIORITY, sos.getPriority());
        values.put(COLUMN_CREATEDTIME, sos.getSos_createdTime());
        values.put(COLUMN_CREATEDBY, sos.getCreatedby());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public List<SosInfo> getAllSOS() {
        List<SosInfo> sosInfos = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                SosInfo sos = new SosInfo();
                sos.setId(Integer.parseInt(cursor.getString(0)));
                sos.setSos_note(cursor.getString(1));
                sos.setPriority(cursor.getString(2));
                sos.setSos_code(cursor.getString(3));
                sos.setSos_title(cursor.getString(4));
               // sos.setGisdata(cursor.getString(5));
              //  sos.setCreatedby(cursor.getString(6));
               // sos.setSos_createdTime(cursor.getString(7));

                // Thêm vào danh sách.
                sosInfos.add(sos);
            } while (cursor.moveToNext());
        }

        return sosInfos;
    }

    public void deleteSos(String sosCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_SOSCODE + " = ?", new String[] { String.valueOf(sosCode) });
        db.delete(TABLE_SOS_MEDIA,COLUMN_SOSCODE+"=?",new String[]{String.valueOf(sosCode)});
        db.close();
    }



    public int update(SosInfo gisTable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, gisTable.getSos_title());
        values.put(COLUMN_NOTE, gisTable.getSos_note());
        values.put(COLUMN_PRIORITY, gisTable.getPriority());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(gisTable.getId())});
    }
    public int updateMedia(SosMedia media)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FILEPATH, media.getFile_path());

        // updating row
        return db.update(TABLE_SOS_MEDIA, values, COLUMN_ID_SOS_MEDIA + " = ?",
                new String[]{String.valueOf(media.getIdsos_media())});
    }
}