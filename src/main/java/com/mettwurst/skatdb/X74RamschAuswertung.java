package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class X74RamschAuswertung extends Activity {

    private SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
    private int spielerzahl = infoSingleton.spielerzahl;  // Local copy
    private int geber = infoSingleton.geber;              //
    int mySolistGewonnen = infoSingleton.solist_gewonnen; //
    private int spielwert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x74_ramsch_auswertung);

        String s = "";

        switch (infoSingleton.ramschAusgang) {
            case SkatInfoSingleton.RAMSCH_1_VERLIERER:
            case SkatInfoSingleton.RAMSCH_2_VERLIERER:
                s += "Ramsch (50)";
                spielwert = 50;
                if (infoSingleton.ramschMultiplikator > 1) {
                    s += "\n" + infoSingleton.ramschMultiplikator + "x";
                    spielwert *= infoSingleton.ramschMultiplikator;
                }
                break;
            case SkatInfoSingleton.RAMSCH_DURCHMARSCH:
                s += "Durchmarsch";
                spielwert = 120;
                break;
        }

        if (infoSingleton.isBock == 1) {
            s += "\nBock x2";
            spielwert *= 2;
        }

        TextView tvAuswertung = (TextView) findViewById(R.id.tvAuswertung);
        tvAuswertung.setText(s);
        TextView tvGesamt = (TextView) findViewById(R.id.tvGesamt);
        tvGesamt.setText("Gesamt: " +((infoSingleton.ramschAusgang == SkatInfoSingleton.RAMSCH_DURCHMARSCH) ? "" : "-") +spielwert);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_x74_ramsch_auswertung, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_OK) {
            infoSingleton.spielwert = this.spielwert;


            if (infoSingleton.ramschAusgang == SkatInfoSingleton.RAMSCH_DURCHMARSCH) {
                infoSingleton.solist = infoSingleton.ramschSolist;
                infoSingleton.solist_gewonnen = 1;
            } else {
                infoSingleton.solist = 0;
                infoSingleton.solist_gewonnen = 0;
            }

            infoSingleton.handspiel = 0;
            infoSingleton.schneider_angesagt = 0;
            infoSingleton.schwarz_angesagt = 0;
            infoSingleton.ouvert = 0;
            infoSingleton.kontra = 0;
            infoSingleton.re = 0;

            infoSingleton.buben_multiplikator = infoSingleton.ramschMultiplikator;

            infoSingleton.schneider_gespielt = 0;
            infoSingleton.schwarz_gespielt = 0;

            int[] punkteSpieler = distributePointsToPlayersRamsch();
            infoSingleton.punkte_sp1 = punkteSpieler[0];
            infoSingleton.gesamt_sp1 += punkteSpieler[0];
            infoSingleton.punkte_sp2 = punkteSpieler[1];
            infoSingleton.gesamt_sp2 += punkteSpieler[1];
            infoSingleton.punkte_sp3 = punkteSpieler[2];
            infoSingleton.gesamt_sp3 += punkteSpieler[2];
            infoSingleton.punkte_sp4 = punkteSpieler[3];
            infoSingleton.gesamt_sp4 += punkteSpieler[3];
            infoSingleton.punkte_sp5 = punkteSpieler[4];
            infoSingleton.gesamt_sp5 += punkteSpieler[4];

            DBController dbCon = new DBController(this);
            dbCon.open();
            dbCon.insert(infoSingleton);
            dbCon.close();

            Intent main = new Intent(X74RamschAuswertung.this, SkatListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.putExtra("intentFlag", SkatListActivity.INTENT_FLAG_RUNDE_EINGETRAGEN);
            startActivity(main);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int[] distributePointsToPlayersRamsch() {
        int solist = infoSingleton.ramschSolist;
        boolean einVerlierer = (infoSingleton.ramschAusgang == SkatInfoSingleton.RAMSCH_1_VERLIERER);
        boolean zweiVerlierer = (infoSingleton.ramschAusgang == SkatInfoSingleton.RAMSCH_2_VERLIERER);
        boolean durchMarsch = (infoSingleton.ramschAusgang == SkatInfoSingleton.RAMSCH_DURCHMARSCH);

        int[] res = new int[5];

        res[0] = (isPlayingSp1() &&
                (       ((solist == 1) && (einVerlierer)) ||        // Als einiger verloren
                        (!(solist == 1) && (zweiVerlierer)) ||      // Zu zweit verloren
                        (!(solist == 1) && (durchMarsch))           // Durchmarsch eines anderen
                )) ? spielwert : 0;
        res[1] = (isPlayingSp2() &&
                (       ((solist == 2) && (einVerlierer)) ||        // Als einiger verloren
                        (!(solist == 2) && (zweiVerlierer)) ||      // Zu zweit verloren
                        (!(solist == 2) && (durchMarsch))           // Durchmarsch eines anderen
                )) ? spielwert : 0;
        res[2] = (isPlayingSp3() &&
                (       ((solist == 3) && (einVerlierer)) ||        // Als einiger verloren
                        (!(solist == 3) && (zweiVerlierer)) ||      // Zu zweit verloren
                        (!(solist == 3) && (durchMarsch))           // Durchmarsch eines anderen
                )) ? spielwert : 0;
        res[3] = (isPlayingSp4() &&
                (       ((solist == 4) && (einVerlierer)) ||        // Als einiger verloren
                        (!(solist == 4) && (zweiVerlierer)) ||      // Zu zweit verloren
                        (!(solist == 4) && (durchMarsch))           // Durchmarsch eines anderen
                )) ? spielwert : 0;
        res[4] = (isPlayingSp5() &&
                (       ((solist == 5) && (einVerlierer)) ||        // Als einiger verloren
                        (!(solist == 5) && (zweiVerlierer)) ||      // Zu zweit verloren
                        (!(solist == 5) && (durchMarsch))           // Durchmarsch eines anderen
                )) ? spielwert : 0;

        return res;
    }

    private boolean isPlayingSp1() {
        return
                (spielerzahl == 3) ||
                        (spielerzahl == 4 && geber != 1) ||
                        (spielerzahl == 5 && geber != 1 && geber != 2)
                ;
    }

    private boolean isPlayingSp2() {
        return
                (spielerzahl == 3) ||
                        (spielerzahl == 4 && geber != 2) ||
                        (spielerzahl == 5 && geber != 2 && geber != 3)
                ;
    }

    private boolean isPlayingSp3() {
        return
                (spielerzahl == 3) ||
                        (spielerzahl == 4 && geber != 3) ||
                        (spielerzahl == 5 && geber != 3 && geber != 4)
                ;
    }

    private boolean isPlayingSp4() {
        return
                (spielerzahl == 4 && geber != 4) ||
                        (spielerzahl == 5 && geber != 4 && geber != 5)
                ;
    }

    private boolean isPlayingSp5() {
        return
                (spielerzahl == 5 && geber != 5 && geber != 1);
    }
}
