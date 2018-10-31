package com.example.chien.location.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chien.location.model.GisTable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    private static final String DATABASE_NAME = "location.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "LOCATION";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_CODE = "CODE";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_GISDATA = "GISDATA";
    private static final String COLUMN_TYPE = "TYPE";
    private static final String COLUMN_ATTRIBUTE = "ATTRIBUTE";
    private static final String COLUMN_PARENT_CODE = "PARENT_CODE";
    private static final String COLUMN_CREATE_BY = "CREATE_BY";
    private static final String COLUMN_CREATE_TIME = "CREATE_TIME";
    private static final String COLUMN_UPDATE_BY = "UPDATE_BY";
    private static final String COLUMN_UPDATE_TIME = "UPDATE_TIME";
    private static final String COLUMN_FLAG = "FLAG";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_CODE + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_GISDATA + " TEXT NOT NULL, "
                + COLUMN_TYPE + " TEXT NOT NULL,"
                + COLUMN_PARENT_CODE + " TEXT NOT NULL,"
                + COLUMN_CREATE_TIME +" TEXT NOT NULL,"
                + COLUMN_UPDATE_TIME + " TEXT NOT NULL)";
/*                + COLUMN_ATTRIBUTE + " TEXT NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL, "

                + COLUMN_CREATE_BY + " TEXT NOT NULL, "

                + COLUMN_UPDATE_BY + " TEXT NOT NULL, "

                + COLUMN_FLAG + " TEXT NOT NULL)";*/

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    public void addData(GisTable gis) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CODE, gis.getCode());
        values.put(COLUMN_NAME, gis.getName());
        values.put(COLUMN_GISDATA, gis.getGisdata());
        values.put(COLUMN_TYPE, gis.getType());
        values.put(COLUMN_PARENT_CODE, gis.getParent());
        values.put(COLUMN_CREATE_TIME, gis.getCreateTime());
        values.put(COLUMN_UPDATE_TIME, gis.getUpdateTime());
/*        values.put(COLUMN_ATTRIBUTE, gis.getAttribute());

        values.put(COLUMN_TITLE, gis.getTitle());
        values.put(COLUMN_CREATE_BY, gis.getCreate_by());

        values.put(COLUMN_UPDATE_BY, gis.getUpdate_by());

        values.put(COLUMN_FLAG, gis.getFlag());*/

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

   /* public GisTable getGis(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID,
                        COLUMN_CODE, COLUMN_NAME, COLUMN_GISDATA, COLUMN_TYPE}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

      GisTable gis = new GisTable(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4),
                cursor.getString(5),cursor.getString(6),
                cursor.getString(7),cursor.getString(8));
        // return note
        return gis;
    }*/

    public List<GisTable> getAllGis() {
        List<GisTable> gisList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                GisTable gis = new GisTable();
                gis.setId(Integer.parseInt(cursor.getString(0)));
                gis.setCode(cursor.getString(1));
                gis.setName(cursor.getString(2));
                gis.setGisdata(cursor.getString(3));
                gis.setType(cursor.getString(4));
                gis.setParent(cursor.getString(5));
                gis.setCreateTime(cursor.getString(6));
                gis.setUpdateTime(cursor.getString(7));
                // Thêm vào danh sách.
                gisList.add(gis);
            } while (cursor.moveToNext());
        }

        return gisList;
    }
    public void deleteGis(int GisID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(GisID) });
        db.close();
    }

    public int update(GisTable gisTable) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, gisTable.getType());
        values.put(COLUMN_GISDATA, gisTable.getGisdata());
        values.put(COLUMN_PARENT_CODE,gisTable.getParent());
        values.put(COLUMN_UPDATE_TIME,gisTable.getUpdateTime());
        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(gisTable.getId())});
    }
}
