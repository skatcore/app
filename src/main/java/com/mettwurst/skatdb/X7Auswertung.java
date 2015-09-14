package com.mettwurst.skatdb;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class X7Auswertung extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x7_auswertung);

        String s = "";
        // TODO: Hier weiter machen

        SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
        int spielwert = 0; // TODO DEBUG: Remove init

        switch(infoSingleton.spiel) {
            case "Null":
                if (infoSingleton.ouvert == 1 && infoSingleton.handspiel == 1) {
                    spielwert = 59;
                    s += "Null Hand Ouvert = 59";
                } else if (infoSingleton.ouvert == 1) {
                    spielwert = 46;
                    s += "Null Ouvert = 46";
                } else if (infoSingleton.handspiel == 1) {
                    spielwert = 35;
                    s += "Null Hand = 35";
                } else {
                    spielwert = 23;
                    s += "Null = 23";
                }
                if (infoSingleton.solist_gewonnen != 1) {
                    spielwert *= 2;
                    s += "\nVerloren x-2";
                }
                if (infoSingleton.kontra == 1 && infoSingleton.re == 1) {
                    spielwert *= 4;
                    s += "\nRe x4";
                } else if (infoSingleton.kontra == 1) {
                    spielwert *= 2;
                    s += "\nKontra x2";
                }
                break;
            // TODO: Add other case (Grand + Farben)
        }

        TextView tvAuswertung = (TextView) findViewById(R.id.tvAuswertung);
        tvAuswertung.setText(s);
        TextView tvGesamt = (TextView) findViewById(R.id.tvGesamt);
        tvGesamt.setText("Gesamt: " +((infoSingleton.solist_gewonnen==1) ? "" : "-") +spielwert);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_x7_auswertung, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
