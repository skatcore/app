package com.mettwurst.skatdb;

import android.provider.BaseColumns;

public final class DBSpielContract {

	public DBSpielContract() {
		// Empty constructor to prevent someone from instantiating this class
	}
	
	public static abstract class Entry implements BaseColumns {
		public static final String TABLE_NAME = "skatSpieleTable";
        public static final String NULL_COLUMN_HACK = "";

        public static final String COL_DATUM                = "Datum";
        public static final String COL_SPIELER             = "Spieler";
	}
}
