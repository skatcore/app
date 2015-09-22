package com.mettwurst.skatdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mettwurst.skatdb.DBContract.Entry;

public class DBController {
	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase database;
	
	public DBController(Context c) {
		context = c;
	}
	
	public DBController open() throws SQLException {
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

    // Reads all needed fields from AddSkatspielActitivy
    public long insert(SkatInfoSingleton s) {
        return insertOrUpdate(-1, s);
    }

    public Cursor fetch() {
        return this.fetch(null);
    }

    public Cursor fetch(String id) {
        Cursor cursor;
        if (id == null) {
            cursor =  database.rawQuery("SELECT * FROM " +Entry.TABLE_NAME, null);
        } else {
            cursor =  database.rawQuery("SELECT * FROM " +Entry.TABLE_NAME +
                    " WHERE _ID = ?", new String[] {String.valueOf(id)});
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchForDate(String date) {
        Cursor cursor = database.rawQuery("SELECT * FROM " +Entry.TABLE_NAME +
                " WHERE " +Entry.COL_DATUM +" = ?", new String[] {date});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, SkatInfoSingleton s) {
        return (int) insertOrUpdate(_id, s);
    }
	
	public void delete(long _id) {
		database.delete(
				Entry.TABLE_NAME, 
				Entry._ID +" = " +_id,  	// whereClause
				null						// whereArgs
				);
	}

    public void deleteLastGameForDate(String date) {
        Cursor c = fetchForDate(date);
        if (c != null) {
            c.moveToLast();
            int id = Integer.valueOf(c.getString(c.getColumnIndex(Entry._ID)));
            delete(id);
        }
    }

    private long insertOrUpdate(long _id, SkatInfoSingleton s) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Entry.COL_DATUM, s.datum);
        values.put(Entry.COL_SPIELERZAHL, s.spielerzahl);
        values.put(Entry.COL_GEBER, s.geber);
        values.put(Entry.COL_SPIELER1, s.spieler1);
        values.put(Entry.COL_SPIELER2, s.spieler2);
        values.put(Entry.COL_SPIELER3, s.spieler3);
        values.put(Entry.COL_SPIELER4, s.spieler4);
        values.put(Entry.COL_SPIELER5, s.spieler5);

        values.put(Entry.COL_SPIEL, s.spiel);

        values.put(Entry.COL_SOLIST, s.solist);

        values.put(Entry.COL_HANDSPIEL, s.handspiel);
        values.put(Entry.COL_SCHNEIDER_ANGESAGT, s.schneider_angesagt);
        values.put(Entry.COL_SCHWARZ_ANGESAGT, s.schwarz_angesagt);
        values.put(Entry.COL_OUVERT, s.ouvert);
        values.put(Entry.COL_KONTRA, s.kontra);
        values.put(Entry.COL_RE, s.re);

        values.put(Entry.COL_BUBEN_MULTIPLIKATOR, s.buben_multiplikator);

        values.put(Entry.COL_SOLIST_GEWONNEN, s.solist_gewonnen);

        values.put(Entry.COL_SCHNEIDER_GESPIELT, s.schneider_gespielt);
        values.put(Entry.COL_SCHWARZ_GESPIELT, s.schwarz_gespielt);

        values.put(Entry.COL_GESAMT_SP1, s.gesamt_sp1);
        values.put(Entry.COL_GESAMT_SP2, s.gesamt_sp2);
        values.put(Entry.COL_GESAMT_SP3, s.gesamt_sp3);
        values.put(Entry.COL_GESAMT_SP4, s.gesamt_sp4);
        values.put(Entry.COL_GESAMT_SP5, s.gesamt_sp5);
        values.put(Entry.COL_PUNKTE_SP1, s.punkte_sp1);
        values.put(Entry.COL_PUNKTE_SP2, s.punkte_sp2);
        values.put(Entry.COL_PUNKTE_SP3, s.punkte_sp3);
        values.put(Entry.COL_PUNKTE_SP4, s.punkte_sp4);
        values.put(Entry.COL_PUNKTE_SP5, s.punkte_sp5);
        values.put(Entry.COL_SPIELWERT, s.spielwert);

        values.put(Entry.COL_BOCK_COUNT, s.bockCount);
        values.put(Entry.COL_BOCK, s.isBock);

        if (_id == -1) {
            return database.insert(Entry.TABLE_NAME, Entry.NULL_COLUMN_HACK, values);
        } else {
            return database.update(             // Return #rows affected
                    Entry.TABLE_NAME,
                    values,
                    Entry._ID +" = " +_id, 		// whereClause
                    null						// whereArgs
            );
        }
    }

    public int getTotalGamesCount() {
        Cursor c = fetch();
        if (c != null && c.getCount() >= 1) {
            return c.getCount();
        } else {
            return 0;
        }
    }

    public String getMostValuableRound() {
        String s = "";
        Cursor c = database.rawQuery("SELECT * FROM " +Entry.TABLE_NAME +
                " WHERE " +Entry.COL_SOLIST_GEWONNEN +" = 1 ORDER BY " +Entry.COL_SPIELWERT +" DESC", null);
        if (c != null && c.getCount() >= 1) {
            c.moveToFirst();
            s += String.valueOf(c.getInt(c.getColumnIndex(DBContract.Entry.COL_SPIELWERT)));
            s += " Punkte (";
            s += c.getString(c.getColumnIndex(DBContract.Entry.COL_SOLIST));
            s += ")";
        } else {
            s = "-";
        }
        return s;
    }
}