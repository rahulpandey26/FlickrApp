package com.robosoft.flickrapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.robosoft.flickrapp.R;
import com.robosoft.flickrapp.utills.Constants;

/**
 * Created by rahul on 27/5/16.
 */
public class DbHelper implements Constants.DatabaseCommand {

    private Context mContext;
    public Data mData;

    public DbHelper(Context context) {
        this.mContext = context;
        mData = new Data(context);
    }

    public long insert(String url) {

        SQLiteDatabase db = mData.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mData.COLUMN_URL, url);

        long f = db.insert(mData.TABLE_NAME, null, contentValues);
        Toast.makeText(mContext, mContext.getResources().getString(R.string.database_image_save), Toast.LENGTH_LONG).show();
        return (f);
    }

    public Cursor getData() {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mData.getWritableDatabase();
        String[] columns = new String[]{mData.ID, mData.COLUMN_URL };
        Cursor cursor = db.query(mData.TABLE_NAME, columns, null, null, null, null, mData.COLUMN_URL);

        if(cursor != null){
            cursor.moveToNext();
        }
        return  cursor;
    }

    public int delete(String i) {
        //String index = String.valueOf(i);
        SQLiteDatabase db = mData.getWritableDatabase();
        int number = db.delete(mData.TABLE_NAME, mData.ID +" = '"+ i +"'",null );
        Toast.makeText(mContext, mContext.getResources().getString(R.string.database_image_delete), Toast.LENGTH_LONG).show();

        return  number;
    }

    public void  deleteAll() {
        SQLiteDatabase db = mData.getWritableDatabase();
        db.execSQL(DELETE_QUERY + mData.TABLE_NAME);
    }

    public static class Data extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "MyDBName";
        public static final int DATABASE_VERSION = 1;
        public static final String TABLE_NAME = "Url";
        public static final String ID = "_id";
        public static final String COLUMN_URL = "url";

        public  static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_URL + " VARCHAR(40));";

        public Data(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(UPGRADE_QUERY + TABLE_NAME);
            onCreate(db);
        }
    }
}
