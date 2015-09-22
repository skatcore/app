package com.mettwurst.skatdb;

import android.provider.BaseColumns;

public final class DBContract {
	
	public DBContract() {
		// Empty constructor to prevent someone from instantiating this class
	}
	
	public static abstract class Entry implements BaseColumns {
		public static final String TABLE_NAME = "skatTable";
        public static final String NULL_COLUMN_HACK = "";

        public static final String COL_DATUM                = "Datum";
        public static final String COL_SPIELERZAHL          = "Spielerzahl";
        public static final String COL_GEBER                = "Geber";
        public static final String COL_SPIELER1             = "Spieler1";
        public static final String COL_SPIELER2             = "Spieler2";
        public static final String COL_SPIELER3             = "Spieler3";
        public static final String COL_SPIELER4             = "Spieler4";
        public static final String COL_SPIELER5             = "Spieler5";

        public static final String COL_SPIEL                = "Spiel";

        public static final String COL_SOLIST               = "Solist";

        public static final String COL_HANDSPIEL            = "Handspiel";
        public static final String COL_SCHNEIDER_ANGESAGT   = "SchneiderAngesagt";
        public static final String COL_SCHWARZ_ANGESAGT     = "SchwarzAngesagt";
        public static final String COL_OUVERT               = "Ouvert";
        public static final String COL_KONTRA               = "Kontra";
        public static final String COL_RE                   = "Re";

        public static final String COL_BUBEN_MULTIPLIKATOR  = "BubenMultiplikator";

        public static final String COL_SOLIST_GEWONNEN      = "SolistGewonnen";

        public static final String COL_SCHNEIDER_GESPIELT   = "SchneiderGespielt";
        public static final String COL_SCHWARZ_GESPIELT     = "SchwarzGespielt";

        public static final String COL_GESAMT_SP1           = "GesamtSp1";
        public static final String COL_GESAMT_SP2           = "GesamtSp2";
        public static final String COL_GESAMT_SP3           = "GesamtSp3";
        public static final String COL_GESAMT_SP4           = "GesamtSp4";
        public static final String COL_GESAMT_SP5           = "GesamtSp5";
        public static final String COL_PUNKTE_SP1           = "PunkteSp1";
        public static final String COL_PUNKTE_SP2           = "PunkteSp2";
        public static final String COL_PUNKTE_SP3           = "PunkteSp3";
        public static final String COL_PUNKTE_SP4           = "PunkteSp4";
        public static final String COL_PUNKTE_SP5           = "PunkteSp5";
        public static final String COL_SPIELWERT            = "SpielwertGesamt";

        public static final String COL_BOCK_COUNT           = "BockCount";
        public static final String COL_BOCK                 = "Bock";
	}
}
