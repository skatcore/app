package com.mettwurst.skatdb;

/**
 * Created by Jo on 21.08.2015.
 */
public class SkatInfoSingleton {

    public static  String datum;
    public static  String spieler1;
    public static  String spieler2;
    public static  String spieler3;
    public static  String spieler4;
    public static  String spieler5;
    public static  int spielerzahl;
    public static  int geber;
    public static  int bock;

    public static  String solist;
    public static  int handspiel;
    public static  int ouvert;
    public static  int schneider_angesagt;
    public static  int schwarz_angesagt;
    public static  int reizwert;
    public static  String spiel;
    public static  int kontra;
    public static  int re;

    public static  String buben = "";
    public static  int schneider_gespielt;
    public static  int schwarz_gespielt;
    public static  int solist_gewonnen = 1;
    public static  int punkte_sp1;
    public static  int punkte_sp2;
    public static  int punkte_sp3;
    public static  int punkte_sp4;
    public static  int punkte_sp5;
    public static  int gesamt_sp1;
    public static  int gesamt_sp2;
    public static  int gesamt_sp3;
    public static  int gesamt_sp4;
    public static  int gesamt_sp5;
    public static  int spielwert;

    public static  int bockCount = 0;


    private static SkatInfoSingleton ourInstance = new SkatInfoSingleton();

    public static SkatInfoSingleton getInstance() {
        return ourInstance;
    }

    private SkatInfoSingleton() {
    }

    public void reset() {
        ourInstance = new SkatInfoSingleton();
        // TODO: Besser init() ???
    }
}
