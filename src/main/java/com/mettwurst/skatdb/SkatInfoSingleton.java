package com.mettwurst.skatdb;

public class SkatInfoSingleton {

    public String datum;
    public int spielerzahl;
    public int geber;
    public String spieler1;
    public String spieler2;
    public String spieler3;
    public String spieler4;
    public String spieler5;

    public String spiel;

    public int solist;

    public int handspiel;
    public int schneider_angesagt;
    public int schwarz_angesagt;
    public int ouvert;
    public int kontra;
    public int re;

    public int buben_multiplikator;

    public int solist_gewonnen;

    public int schneider_gespielt;
    public int schwarz_gespielt;

    public int gesamt_sp1;
    public int gesamt_sp2;
    public int gesamt_sp3;
    public int gesamt_sp4;
    public int gesamt_sp5;
    public int punkte_sp1;
    public int punkte_sp2;
    public int punkte_sp3;
    public int punkte_sp4;
    public int punkte_sp5;
    public int spielwert;

    public int bockCount;
    public int isBock;

    private static SkatInfoSingleton ourInstance;

    public static SkatInfoSingleton getInstance() {
        return ourInstance;
    }

    private SkatInfoSingleton() {
    }

    public static void init(String datum, int spielerzahl, int geber, String spieler1,
                            String spieler2, String spieler3, String spieler4, String spieler5,
                            int gesamt_sp1, int gesamt_sp2, int gesamt_sp3, int gesamt_sp4,
                            int gesamt_sp5, int bockCount) {
        ourInstance = new SkatInfoSingleton();

        ourInstance.datum = datum;
        ourInstance.spielerzahl = spielerzahl;
        ourInstance.geber = geber;
        ourInstance.spieler1 = spieler1;
        ourInstance.spieler2 = spieler2;
        ourInstance.spieler3 = spieler3;
        ourInstance.spieler4 = spieler4;
        ourInstance.spieler5 = spieler5;
        ourInstance.gesamt_sp1 = gesamt_sp1;
        ourInstance.gesamt_sp2 = gesamt_sp2;
        ourInstance.gesamt_sp3 = gesamt_sp3;
        ourInstance.gesamt_sp4 = gesamt_sp4;
        ourInstance.gesamt_sp5 = gesamt_sp5;
        ourInstance.bockCount = bockCount;
        ourInstance.isBock = (bockCount > 0) ? 1 : 0;
    }
}
