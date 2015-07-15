package com.mettwurst.skatdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mettwurst.skatdb.DBSpielContract.Entry;

public class DBSpielController {
	private DBSpielHelper dbSpielHelper;
	private Context context;
	private SQLiteDatabase database;

	public DBSpielController(Context c) {
		context = c;
	}
	
	public DBSpielController open() throws SQLException {
		dbSpielHelper = new DBSpielHelper(context);
		database = dbSpielHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbSpielHelper.close();
	}

    public long insert(String date, String[] spieler) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Entry.COL_DATUM, date);
        String s = spieler[0]
                +"," +spieler[1]
                +"," +spieler[2];
        if (spieler.length >= 4 && spieler[3] != null && !spieler[3].equals("")) {
            s += "," +spieler[3];
        }
        if (spieler.length >= 5 && spieler[4] != null && !spieler[4].equals("")) {
            s += "," +spieler[4];
        }
        
        values.put(Entry.COL_SPIELER, s);
        return database.insert(Entry.TABLE_NAME, Entry.NULL_COLUMN_HACK, values);
    }

    public Cursor fetch() {
        Cursor cursor = database.rawQuery("SELECT * FROM " +Entry.TABLE_NAME + " ORDER BY " +Entry.COL_DATUM +" DESC", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

	public void delete(long _id) {
        database.delete(
                Entry.TABLE_NAME,
                Entry._ID +" = " +_id,  	// whereClause
                null						// whereArgs
        );
    }

    public void delete(String date) {
        database.delete(
                Entry.TABLE_NAME,
                Entry.COL_DATUM +" = " +date,  	// whereClause
                null						// whereArgs
        );
    }
}