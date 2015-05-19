package com.mettwurst.skatdb;

import android.app.Activity;
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
    public long insert(ActivityWithSkatinfo a) {
        return insertOrUpdate(-1, a);
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

    public int update(long _id, ActivityWithSkatinfo a) {
        return (int) insertOrUpdate(_id, a);
    }
	
	public void delete(long _id) {
		database.delete(
				Entry.TABLE_NAME, 
				Entry._ID +" = " +_id,  	// whereClause
				null						// whereArgs
				);
	}

    public void clearTable() {
        database.delete(Entry.TABLE_NAME, null, null);
    }

    private long insertOrUpdate(long _id, ActivityWithSkatinfo a) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Entry.COL_DATUM, a.datum);
        values.put(Entry.COL_SPIELER1, a.spieler1);
        values.put(Entry.COL_SPIELER2, a.spieler2);
        values.put(Entry.COL_SPIELER3, a.spieler3);
        values.put(Entry.COL_SPIELER4, a.spieler4);
        values.put(Entry.COL_SPIELER5, a.spieler5);
        values.put(Entry.COL_SPIELERZAHL, a.spielerzahl);
        values.put(Entry.COL_GEBER, a.geber);
        values.put(Entry.COL_SOLIST, a.solist);
        values.put(Entry.COL_BUBEN, a.buben);
        values.put(Entry.COL_HANDSPIEL, a.handspiel);
        values.put(Entry.COL_OUVERT, a.ouvert);
        values.put(Entry.COL_SCHNEIDER_ANGESAGT, a.schneider_angesagt);
        values.put(Entry.COL_SCHWARZ_ANGESAGT, a.schwarz_angesagt);
        values.put(Entry.COL_REIZWERT, a.reizwert);
        values.put(Entry.COL_SPIEL, a.spiel);
        values.put(Entry.COL_SCHNEIDER_GESPIELT, a.schneider_gespielt);
        values.put(Entry.COL_SCHWARZ_GESPIELT, a.schwarz_gespielt);
        values.put(Entry.COL_PUNKTE_RE, a.punkte_re);
        values.put(Entry.COL_SOLIST_GEWONNEN, a.solist_gewonnen);
        values.put(Entry.COL_KONTRA, a.kontra);
        values.put(Entry.COL_RE, a.re);
        values.put(Entry.COL_BOCK, a.bock);
        values.put(Entry.COL_PUNKTE_SP1, a.punkte_sp1);
        values.put(Entry.COL_PUNKTE_SP2, a.punkte_sp2);
        values.put(Entry.COL_PUNKTE_SP3, a.punkte_sp3);
        values.put(Entry.COL_PUNKTE_SP4, a.punkte_sp4);
        values.put(Entry.COL_PUNKTE_SP5, a.punkte_sp5);
        values.put(Entry.COL_GESAMT_SP1, a.gesamt_sp1);
        values.put(Entry.COL_GESAMT_SP2, a.gesamt_sp2);
        values.put(Entry.COL_GESAMT_SP3, a.gesamt_sp3);
        values.put(Entry.COL_GESAMT_SP4, a.gesamt_sp4);
        values.put(Entry.COL_GESAMT_SP5, a.gesamt_sp5);
        values.put(Entry.COL_JUNGFRAU_ANGESAGT1, a.jungfrau_angesagt1);
        values.put(Entry.COL_JUNGFRAU_ANGESAGT2, a.jungfrau_angesagt2);
        values.put(Entry.COL_JUNGFRAU_ANGESAGT3, a.jungfrau_angesagt3);
        values.put(Entry.COL_JUNGFRAU_ANGESAGT4, a.jungfrau_angesagt4);
        values.put(Entry.COL_JUNGFRAU_ANGESAGT5, a.jungfrau_angesagt5);
        values.put(Entry.COL_SPIELWERT, a.spielwert);
        values.put(Entry.COL_BOCK_RAMSCH_STATUS, a.bockRamschStatus);
        values.put(Entry.COL_BOCK_COUNT, a.bockCount);
        values.put(Entry.COL_RAMSCH_COUNT, a.ramschCount);

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
}