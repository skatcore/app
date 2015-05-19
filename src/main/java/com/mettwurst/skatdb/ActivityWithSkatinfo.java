package com.mettwurst.skatdb;

import android.app.Activity;

/**
 * Created by Jo on 11.05.2015.
 * This Activity holds all needed fields for database operations
 */
public abstract class ActivityWithSkatinfo extends Activity {
    public String datum;
    public String spieler1;
    public String spieler2;
    public String spieler3;
    public String spieler4;
    public String spieler5;
    public int spielerzahl;
    public int geber;
    public int bock;

    public String solist;
    public int handspiel;
    public int ouvert;
    public int schneider_angesagt;
    public int schwarz_angesagt;
    public int reizwert;
    public String spiel;
    public int kontra;
    public int re;
    public int jungfrau_angesagt1;
    public int jungfrau_angesagt2;
    public int jungfrau_angesagt3;
    public int jungfrau_angesagt4;
    public int jungfrau_angesagt5;

    public String buben = "";
    public int schneider_gespielt;
    public int schwarz_gespielt;
    public int punkte_re = 0;
    public int solist_gewonnen = 1;
    public int punkte_sp1;
    public int punkte_sp2;
    public int punkte_sp3;
    public int punkte_sp4;
    public int punkte_sp5;
    public int gesamt_sp1;
    public int gesamt_sp2;
    public int gesamt_sp3;
    public int gesamt_sp4;
    public int gesamt_sp5;
    public int spielwert;

    public int bockRamschStatus = 0; // 0: Normal, 1: Bock, 2: Ramsch
    public int bockCount = 0;
    public int ramschCount = 0;
}
