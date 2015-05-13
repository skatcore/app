package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class AddSkatrundeActivity extends ActivityWithSkatinfo {

    private boolean bockUndRamsch = false;

    private DBController dbCon;

    private TextWatcher twUpdateAll = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateAll();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Skatspiel");

        // Read all collected information delivered via Intent
        Intent intent = getIntent();

        // From SkatListActivity
        datum = intent.getStringExtra("datum");
        spieler1 = intent.getStringExtra("spieler1");
        spieler2 = intent.getStringExtra("spieler2");
        spieler3 = intent.getStringExtra("spieler3");
        spieler4 = intent.getStringExtra("spieler4");
        spieler5 = intent.getStringExtra("spieler5");
        spielerzahl = intent.getIntExtra("spielerzahl", -1);
        geber = intent.getIntExtra("geber", -1);
        bock = intent.getIntExtra("bock", 0);

        // From AddAnsageActivity
        solist = intent.getStringExtra("solist");
        handspiel = intent.getIntExtra("handspiel", 0);
        ouvert = intent.getIntExtra("ouvert", 0);
        schneider_angesagt = intent.getIntExtra("schneider_angesagt", 0);
        schwarz_angesagt = intent.getIntExtra("schwarz_angesagt", 0);
        reizwert = intent.getIntExtra("reizwert", 0);
        spiel = intent.getStringExtra("spiel");
        kontra = intent.getIntExtra("kontra", 0);
        re = intent.getIntExtra("re", 0);
        jungfrau_angesagt1 = intent.getIntExtra("jungfrau_angesagt1", 0);
        jungfrau_angesagt2 = intent.getIntExtra("jungfrau_angesagt2", 0);
        jungfrau_angesagt3 = intent.getIntExtra("jungfrau_angesagt3", 0);
        jungfrau_angesagt4 = intent.getIntExtra("jungfrau_angesagt4", 0);
        jungfrau_angesagt5 = intent.getIntExtra("jungfrau_angesagt5", 0);

        // Current total points. We will add points from current game to this values
        gesamt_sp1 = intent.getIntExtra("gesamt_sp1", 0);
        gesamt_sp2 = intent.getIntExtra("gesamt_sp2", 0);
        gesamt_sp3 = intent.getIntExtra("gesamt_sp3", 0);
        gesamt_sp4 = intent.getIntExtra("gesamt_sp4", 0);
        gesamt_sp5 = intent.getIntExtra("gesamt_sp5", 0);

        // Show layout depending on played game
        if (spiel.equals("Null")) {
            setContentView(R.layout.activity_add_skatspiel_null);
            updateAll();

            RadioGroup rg = (RadioGroup) findViewById(R.id.rgGewinner);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    updateAll();
                }
            });
        } else if (spiel.equals("Ramsch")) {
            setContentView(R.layout.activity_add_skatspiel_ramsch);

            // Fill the name fields
            TextView sp1 = (TextView) findViewById(R.id.tvSpieler1);
            TextView sp2 = (TextView) findViewById(R.id.tvSpieler2);
            TextView sp3 = (TextView) findViewById(R.id.tvSpieler3);
            String[] s = getPlayingPlayersInOrder(geber, spielerzahl, spieler1, spieler2, spieler3, spieler4, spieler5);
            sp1.setText(s[0]);
            sp2.setText(s[1]);
            sp3.setText(s[2]);

            EditText edRamschPunkteSp1 = (EditText) findViewById(R.id.edRamschpunkteSp1);
            EditText edRamschPunkteSp2 = (EditText) findViewById(R.id.edRamschpunkteSp2);
            EditText edRamschPunkteSp3 = (EditText) findViewById(R.id.edRamschpunkteSp3);
            edRamschPunkteSp1.addTextChangedListener(twUpdateAll);
            edRamschPunkteSp2.addTextChangedListener(twUpdateAll);
            edRamschPunkteSp3.addTextChangedListener(twUpdateAll);
        } else {
            // Farbspiel oder Grand
            setContentView(R.layout.activity_add_skatspiel);
            CompoundButton.OnCheckedChangeListener myListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    updateAll();
                }
            };
            CheckBox cbKreuz = (CheckBox) findViewById(R.id.cbKreuz);
            CheckBox cbPik = (CheckBox) findViewById(R.id.cbPik);
            CheckBox cbHerz = (CheckBox) findViewById(R.id.cbHerz);
            CheckBox cbKaro = (CheckBox) findViewById(R.id.cbKaro);
            cbKreuz.setOnCheckedChangeListener(myListener);
            cbPik.setOnCheckedChangeListener(myListener);
            cbHerz.setOnCheckedChangeListener(myListener);
            cbKaro.setOnCheckedChangeListener(myListener);

            final EditText edPunkteRe = (EditText) findViewById(R.id.edPunkteRe);
            edPunkteRe.addTextChangedListener(twUpdateAll);
        }

        twUpdateAll.afterTextChanged(null);
        dbCon = new DBController(this);
        dbCon.open();
    }

    public static String[] getPlayingPlayersInOrder(int geber, int spielerzahl, String spieler1, String spieler2, String spieler3, String spieler4, String spieler5) {
        String[] res = new String[3];

        switch (geber) {
            case 1:
                res[0] = spieler2;
                res[1] = spieler3;
                res[2] = (spielerzahl >= 4) ?
                        spieler4 : spieler1;
                break;
            case 2:
                res[0] = spieler3;
                switch (spielerzahl) {
                    case 3:
                        res[1] = spieler1;
                        res[2] = spieler2;
                        break;
                    case 4:
                        res[1] = spieler4;
                        res[2] = spieler1;
                        break;
                    case 5:
                        res[1] = spieler4;
                        res[2] = spieler5;
                        break;
                }
                break;
            case 3:
                switch (spielerzahl) {
                    case 3:
                        res[0] = spieler1;
                        res[1] = spieler2;
                        res[2] = spieler3;
                        break;
                    case 4:
                        res[0] = spieler4;
                        res[1] = spieler1;
                        res[2] = spieler2;
                        break;
                    case 5:
                        res[0] = spieler4;
                        res[1] = spieler5;
                        res[2] = spieler1;
                        break;
                }
                break;
            case 4:
                if (spielerzahl == 4) {
                    res[0] = spieler1;
                    res[0] = spieler2;
                    res[0] = spieler3;
                } else {
                    // spielerzahl == 5
                    res[0] = spieler5;
                    res[0] = spieler1;
                    res[0] = spieler2;
                }
                break;
            case 5:
                res[0] = spieler1;
                res[0] = spieler2;
                res[0] = spieler3;
                break;
        }

        return res;
    }

    private void updateAll() {
        if (spiel.equals("Ramsch")) {
            updateRamsch();
        } else if (spiel.equals("Null")) {
            updateNull();
        } else {
            updateNormal();
        }
    }

    private void updateNormal() {
        // TODO: Schwarz per CheckBox, da 0 bzw 120 Punkte nicht Schwarz implizieren
        TextView tv = (TextView) findViewById(R.id.tvSpielAuswertung);
        CheckBox cbKreuz = (CheckBox) findViewById(R.id.cbKreuz);
        CheckBox cbPik = (CheckBox) findViewById(R.id.cbPik);
        CheckBox cbHerz = (CheckBox) findViewById(R.id.cbHerz);
        CheckBox cbKaro = (CheckBox) findViewById(R.id.cbKaro);
        EditText edPunkteRe = (EditText) findViewById(R.id.edPunkteRe);

        boolean kreuz, pik, herz, karo;
        kreuz = cbKreuz.isChecked();
        pik = cbPik.isChecked();
        herz = cbHerz.isChecked();
        karo = cbKaro.isChecked();

        // Bestimmung der Buben
        if (kreuz)
            if (pik)
                if (herz)
                    if (karo)
                        buben = "Mit 4";
                    else buben = "Mit 3";
                else buben = "Mit 2";
            else buben = "Mit 1";
        else // Ohne
            if (!pik)
                if (!herz)
                    if (!karo)
                        buben = "Ohne 4";
                    else buben = "Ohne 3";
                else buben = "Ohne 2";
            else buben = "Ohne 1";



        int grundwert;
        if (spiel.equals("Kreuz"))      grundwert = 12;
        else if (spiel.equals("Pik"))   grundwert = 11;
        else if (spiel.equals("Herz"))  grundwert = 10;
        else if (spiel.equals("Karo"))  grundwert = 9;
        else                            grundwert = 24; // Grand

        int multiplikator;
        if (buben.equals("Mit 1") || buben.equals("Ohne 1"))        multiplikator = 2;
        else if (buben.equals("Mit 2") || buben.equals("Ohne 2"))   multiplikator = 3;
        else if (buben.equals("Mit 3") || buben.equals("Ohne 3"))   multiplikator = 4;
        else                                                        multiplikator = 5;

        String s = solist +": " +buben +" Spiel " +multiplikator;

        int punkte_re = readIntOrZero(edPunkteRe);

        if (schwarz_angesagt > 0) {
            s += " Schwarz angesagt";
            multiplikator += 4;

            if (punkte_re >= 120) {
                // Schwarz gespielt
                schneider_gespielt = 1;
                schwarz_gespielt = 1;
                solist_gewonnen = 1;
                multiplikator += 2;
            } else {
                // Solist verloren
                schneider_gespielt = (punkte_re > 90) ? 1 : 0;
                schwarz_gespielt = 0;
                solist_gewonnen = 0;
                multiplikator *= 2;
                s += ", VERLOREN";
            }
        } else if (ouvert > 0 || schneider_angesagt > 0) {
            if (ouvert > 0) {
                s += " Ouvert";
                multiplikator += 3;
            } else {
                s += " Schneider angesagt";
                multiplikator += 2;
            }

            if (punkte_re > 90) {
                // Schneider gepspielt
                schneider_gespielt = 1;
                schwarz_gespielt = 0;
                solist_gewonnen = 1;
                multiplikator += 1;
            } else {
                schneider_gespielt = 0;
                schwarz_gespielt = 0;
                solist_gewonnen = 0;
                multiplikator *= 2;
                s += ", VERLOREN";
            }
        } else {
            if (handspiel > 0) {
                s += " Hand";
                multiplikator += 1;
            }

            // Normale Auswertung
            if (punkte_re >= 120) {
                schneider_gespielt = 1;
                schwarz_gespielt = 1;
                s += " Schwarz";
                multiplikator += 2;
            } else if (punkte_re > 90) {
                schneider_gespielt = 1;
                schwarz_gespielt = 0;
                s += " Schneider";
                multiplikator += 1;
            } else {
                schneider_gespielt = 0;
                schwarz_gespielt = 0;
            }

            if (punkte_re > 60) {
                solist_gewonnen = 1;
            } else {
                if (punkte_re == 60) {
                    // Spaltarsch
                    bockUndRamsch = true;
                }
                solist_gewonnen = 0;
                multiplikator *= 2;
                s += ", VERLOREN";
                if (punkte_re <= 0) {
                    s += " Schwarz";
                    multiplikator += 2;
                    schneider_gespielt = 1;
                    schwarz_gespielt = 1;
                } else if (punkte_re < 30) {
                    s += " Schneider";
                    multiplikator += 1;
                    schneider_gespielt = 1;
                }
            }
        }

        if ((solist_gewonnen > 0) && (multiplikator * grundwert < reizwert)) {
            s += ", VERLOREN (ÜBERREIZT)";
            solist_gewonnen = 0;
            multiplikator *= 2;
        }

        if (kontra > 0) {
            multiplikator *= 2;
            s += " Kontra";
        }
        if (re > 0) {
            multiplikator *= 2;
            s += " Re";
        }
        if (bock > 0) {
            multiplikator *= 2;
            s += " Bock";
        }

        spielwert = grundwert * multiplikator;
        s += " " +multiplikator +" " +spiel +" = " +spielwert;
        tv.setText(s);

        if (kontra > 0 && solist_gewonnen == 0) {
            bockUndRamsch = true;
        }
        distributePointsToLosers();
    }

    // Setzt alle Felder und macht ein Update der Textausgabe
    private void updateRamsch() {
        TextView tv = (TextView) findViewById(R.id.tvSpielAuswertung);
        EditText edPunkte1 = (EditText) findViewById(R.id.edRamschpunkteSp1);
        EditText edPunkte2 = (EditText) findViewById(R.id.edRamschpunkteSp2);
        EditText edPunkte3 = (EditText) findViewById(R.id.edRamschpunkteSp3);

        buben = "";
        schneider_gespielt = 0;
        schwarz_gespielt = 0;
        punkte_re = 0;
        solist_gewonnen = 0;

        String[] players = getPlayingPlayersInOrder(geber, spielerzahl, spieler1, spieler2, spieler3, spieler4, spieler5);
        int[] ramschPunkte = {
                readIntOrZero(edPunkte1),
                readIntOrZero(edPunkte2),
                readIntOrZero(edPunkte3)};

        punkte_sp1 = 0;
        punkte_sp2 = 0;
        punkte_sp3 = 0;
        punkte_sp4 = 0;
        punkte_sp5 = 0;

        // Durchmarsch?
        for (int i = 0; i < 3; i++) {
            if (ramschPunkte[i] >= 120 ) {
                spielwert = 120;

                if (isPlayingSp1() && !players[i].equals(spieler1)) {
                    punkte_sp1 = 120;// Spieler spielt mit, hat aber NICHT den Durchmarsch gemacht -> Punkte
                    tv.setText(spieler1+ ": Durchmarsch= 120");
                }
                if (isPlayingSp2() && !players[i].equals(spieler2)) {
                    punkte_sp2 = 120;
                    tv.setText(spieler2+ ": Durchmarsch= 120");
                }
                if (isPlayingSp3() && !players[i].equals(spieler3)) {
                    punkte_sp3 = 120;
                    tv.setText(spieler3+ ": Durchmarsch= 120");
                }
                if (isPlayingSp4() && !players[i].equals(spieler4)) {
                    punkte_sp4 = 120;
                    tv.setText(spieler4+ ": Durchmarsch= 120");
                }
                if (isPlayingSp5() && !players[i].equals(spieler5)) {
                    punkte_sp5 = 120;
                    tv.setText(spieler5+ ": Durchmarsch= 120");
                }

                return; // Done. (Only 1 Durchmarsch allowed)
            }
        }

        // Falsche Jungfrau-Ansage?
        for (int i = 0; i < 3; i++) {
            if (ramschPunkte[i] > 0){
                if (players[i].equals(spieler1) && jungfrau_angesagt1 > 0) {
                    // Spieler hat eine falsche Jungfrau-Ansage gemacht
                    spielwert = 120;
                    punkte_sp1 = 120;
                    tv.setText(spieler1 +": Jungfrau angesagt, VERLOREN = 120");
                    return;
                }
                if (players[i].equals(spieler2) && jungfrau_angesagt2 > 0) {
                    spielwert = 120;
                    punkte_sp2 = 120;
                    tv.setText(spieler2 +": Jungfrau angesagt, VERLOREN = 120");
                    return;
                }
                if (players[i].equals(spieler3) && jungfrau_angesagt3 > 0) {
                    spielwert = 120;
                    punkte_sp3 = 120;
                    tv.setText(spieler3 +": Jungfrau angesagt, VERLOREN = 120");
                    return;
                }
                if (players[i].equals(spieler4) && jungfrau_angesagt4 > 0) {
                    spielwert = 120;
                    punkte_sp4 = 120;
                    tv.setText(spieler4 +": Jungfrau angesagt, VERLOREN = 120");
                    return;
                }
                if (players[i].equals(spieler5) && jungfrau_angesagt5 > 0) {
                    spielwert = 120;
                    punkte_sp5 = 120;
                    tv.setText(spieler5 +": Jungfrau angesagt, VERLOREN = 120");
                    return;
                }
            }
        }

        int max = 0;
        for (int i : ramschPunkte) {
            if (i > max) {
                max = i;
            }
        }

        spielwert = max;
        String s = "Ramsch";
        for (int i : ramschPunkte) {
            if (i == 0) {
                // Es gibt nur max eine Jungrau
                s += " Jungfrau";
                spielwert *= 2;
            }
        }
        if (jungfrau_angesagt1 > 0 ||
            jungfrau_angesagt2 > 0 ||
            jungfrau_angesagt3 > 0 ||
            jungfrau_angesagt4 > 0 ||
            jungfrau_angesagt5 > 0) {
                // Keine falsche Jungfrau-Ansage mehr möglich
                s += " angesagt";
                spielwert *= 2;
        }
        if (bock > 0) {
            s += " Bock";
            spielwert *= 2;
        }

        int verliererCount = 0;
        for (int i = 0; i < 3; i++) {
            if (ramschPunkte[i] == max) {
                // Spieler hat verloren
                verliererCount++;

                if (players[i].equals(spieler1)) {
                    punkte_sp1 = spielwert;
                    s = spieler1 +": " +s;
                }
                if (players[i].equals(spieler2)) {
                    punkte_sp2 = spielwert;
                    s = spieler2 +": " +s;
                }
                if (players[i].equals(spieler3)) {
                    punkte_sp3 = spielwert;
                    s = spieler3 +": " +s;
                }
                if (players[i].equals(spieler4)) {
                    punkte_sp4 = spielwert;
                    s = spieler4 +": " +s;
                }
                if (players[i].equals(spieler5)) {
                    punkte_sp5 = spielwert;
                    s = spieler5 +": " +s;
                }
            }
        }

        tv.setText(s);
        if (verliererCount >= 2) {
            bockUndRamsch = true;
        }
    }

    // Setzt alle Felder und macht ein Update der Textausgabe
    private void updateNull() {
        RadioButton rbGewonnen = (RadioButton) findViewById(R.id.rbGewonnen);
        TextView tv = (TextView) findViewById(R.id.tvSpielAuswertung);

        buben = "";
        schneider_gespielt = 0;
        schwarz_gespielt = 0;
        punkte_re = 0;
        solist_gewonnen = (rbGewonnen.isChecked() ? 1 : 0);

        String s = solist +": Null";

        int grundwert;
            if (handspiel > 0 && ouvert > 0) {
                grundwert = 59;
                s += " Ouvert Hand";
            }
            else if (ouvert > 0) {
                grundwert = 46;
                s += " Ouvert";
            }
            else if (handspiel > 0) {
                grundwert = 35;
                s += " Hand";
            }
            else grundwert = 23;

        if (kontra > 0) {
            spielwert *= 2;
            s += " Kontra";
        }
        if (re > 0) {
            spielwert *=2;
            s += "Re";
        }
        if (bock > 0) {
            spielwert *= 2;
            s += " Bock";
        }
        if (solist_gewonnen > 0) {
            spielwert = grundwert;
        } else{
            spielwert = 2 * grundwert;
            s += ", VERLOREN";
        }

        s += " = " +((solist_gewonnen > 0) ? "" : "-") +spielwert;
        tv.setText(s);

        if (kontra > 0 && solist_gewonnen == 0) {
            bockUndRamsch = true;
        }
        distributePointsToLosers();
    }

    // Vergibt die Punkte an alle Spieler entsprechend
    private void distributePointsToLosers() {
        punkte_sp1 = (isPlayingSp1() &&
                (       (spieler1.equals(solist) && !(solist_gewonnen > 0)) ||  // Solist but lost game
                        (!spieler1.equals(solist) && (solist_gewonnen > 0))     // Not solist but solist won
                )) ? spielwert : 0;
        punkte_sp2 = (isPlayingSp2() &&
                (       (spieler2.equals(solist) && !(solist_gewonnen > 0)) ||
                        (!spieler2.equals(solist) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
        punkte_sp3 = (isPlayingSp3() &&
                (       (spieler3.equals(solist) && !(solist_gewonnen > 0)) ||
                        (!spieler3.equals(solist) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
        punkte_sp4 = (isPlayingSp4() &&
                (       (spieler4.equals(solist) && !(solist_gewonnen > 0)) ||
                        (!spieler4.equals(solist) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
        punkte_sp5 = (isPlayingSp5() &&
                (       (spieler5.equals(solist) && !(solist_gewonnen > 0)) ||
                        (!spieler5.equals(solist) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_skatspiel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuAddSkatspiel) {
            gesamt_sp1 += punkte_sp1;
            gesamt_sp2 += punkte_sp2;
            gesamt_sp3 += punkte_sp3;
            gesamt_sp4 += punkte_sp4;
            gesamt_sp5 += punkte_sp5;

            dbCon.insert(this); // DBController reads the public fields from this instance

            Intent main = new Intent(AddSkatrundeActivity.this, SkatListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.putExtra("bockUndRamsch", bockUndRamsch);
            main.putExtra("warRamsch", spiel.equals("Ramsch"));
            main.putExtra("rundeEingetragen", true);
            startActivity(main);
        }

        return super.onOptionsItemSelected(item);
    }

    private int readIntOrZero(EditText ed) {
        int res = 0;
        try {
            res = Integer.valueOf(ed.getText().toString());
        } catch (Exception e) {
            // Do nothing
        }
        return res;
    }
}
