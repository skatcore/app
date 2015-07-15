package com.mettwurst.skatdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mettwurst.skatdb.DBSpielContract.Entry;

public class DBSpielHelper extends SQLiteOpenHelper {

	private static final String TEXT_TYPE   = " VARCHAR";  // Text type to use (VARCHAR / TEXT).
	private static final String COMMA_SEP   = ", ";        // Comma separator to use.

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBSpielContract.Entry.TABLE_NAME +" (" +
                    Entry._ID 				            +" INTEGER PRIMARY KEY" 	+COMMA_SEP +
                    Entry.COL_DATUM     	            + " DATETIME"  				+COMMA_SEP +
                    Entry.COL_SPIELER    	            +TEXT_TYPE +
                    " )";

	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

	public static final int DATABASE_VERSION = 7;
	public static String DATABASE_NAME = "skat007.db";


	public DBSpielHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
