package com.mettwurst.skatdb;

import com.mettwurst.skatdb.DBContract.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String TEXT_TYPE   = " VARCHAR";  // Text type to use (VARCHAR / TEXT).
    private static final String INT_TYPE    = " INTEGER";
    private static final String BOOL_TYPE   = " INTEGER";
	private static final String COMMA_SEP   = ", ";        // Comma separator to use.

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +Entry.TABLE_NAME +" (" +
                    Entry._ID 				            +" INTEGER PRIMARY KEY" 	+COMMA_SEP +
                    Entry.COL_DATUM     	            + " DATETIME"  				+COMMA_SEP +
                    Entry.COL_SPIELER1    	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIELER2    	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIELER3    	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIELER4    	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIELER5    	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIELERZAHL    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_GEBER    	                +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_SOLIST    	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_BUBEN     	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_HANDSPIEL    	            +BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_OUVERT    	            +BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_SCHNEIDER_ANGESAGT    	+BOOL_TYPE					+COMMA_SEP +
                    Entry.COL_SCHWARZ_ANGESAGT    	    +BOOL_TYPE					+COMMA_SEP +
                    Entry.COL_REIZWERT    	            +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIEL     	            +TEXT_TYPE 					+COMMA_SEP +
                    Entry.COL_SCHNEIDER_GESPIELT    	+BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_SCHWARZ_GESPIELT    	    +BOOL_TYPE					+COMMA_SEP +
                    Entry.COL_PUNKTE_RE    	            +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_SOLIST_GEWONNEN    	    +BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_KONTRA    	            +BOOL_TYPE					+COMMA_SEP +
                    Entry.COL_RE        	            +BOOL_TYPE					+COMMA_SEP +
                    Entry.COL_BOCK      	            +BOOL_TYPE					+COMMA_SEP +
                    Entry.COL_PUNKTE_SP1    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_PUNKTE_SP2    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_PUNKTE_SP3    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_PUNKTE_SP4    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_PUNKTE_SP5    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_GESAMT_SP1    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_GESAMT_SP2    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_GESAMT_SP3    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_GESAMT_SP4    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_GESAMT_SP5    	        +INT_TYPE 					+COMMA_SEP +
                    Entry.COL_JUNGFRAU_ANGESAGT1    	+BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_JUNGFRAU_ANGESAGT2    	+BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_JUNGFRAU_ANGESAGT3    	+BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_JUNGFRAU_ANGESAGT4    	+BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_JUNGFRAU_ANGESAGT5    	+BOOL_TYPE 					+COMMA_SEP +
                    Entry.COL_SPIELWERT         	    +INT_TYPE +
                    " )";
	
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

	public static final int DATABASE_VERSION = 4;
	public static String DATABASE_NAME = "skat005.db";

	
	public DBHelper (Context context) {
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
