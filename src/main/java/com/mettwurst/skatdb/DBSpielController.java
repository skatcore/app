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
        values.put(Entry.COL_SPIELER1, spieler[0]);
        values.put(Entry.COL_SPIELER2, spieler[1]);
        values.put(Entry.COL_SPIELER3, spieler[2]);
        String spieler4 = spieler[3];
        if (spieler4 == null) {
            spieler4 = "";
        }
        values.put(Entry.COL_SPIELER4, spieler4);
        String spieler5 = spieler[4];
        if (spieler5 == null) {
            spieler5 = "";
        }
        values.put(Entry.COL_SPIELER5, spieler5);

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