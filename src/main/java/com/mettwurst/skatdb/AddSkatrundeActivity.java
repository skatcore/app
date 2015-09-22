package com.mettwurst.skatdb;

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

// TODO:
// - Bock und Ramsch Status in DB
// - Bock Count in DB
// - Ramsch Count in DB


public class AddSkatrundeActivity extends ActivityWithSkatinfo {
/*
    private DBController dbCon;
    private CheckBox cbSchwarzGespielt;
    private int ramschVerliererCount = 1;

    private TextWatcher twUpdateAll = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null) {
                if (readIntOrZero(s.toString()) > 120) {
                    s.clear();
                    s.append("120");
                }
            }

            if (s != null && !spiel.equals("Ramsch")) {
                final int value = readIntOrZero(s.toString());
                if (value != 0 && value != 120) {
                    cbSchwarzGespielt.setChecked(false);
                    cbSchwarzGespielt.setEnabled(false);
                } else {
                    cbSchwarzGespielt.setEnabled(true);
                }
            }
            updateAll();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setTitle((!"".equals(solist) ? solist + ": " : "") +spiel);
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
            cbSchwarzGespielt = (CheckBox) findViewById(R.id.cbSchwarzGespielt);
            cbSchwarzGespielt.setOnCheckedChangeListener(myListener);

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
                    res[1] = spieler2;
                    res[2] = spieler3;
                } else {
                    // spielerzahl == 5
                    res[0] = spieler5;
                    res[1] = spieler1;
                    res[2] = spieler2;
                }
                break;
            case 5:
                res[0] = spieler1;
                res[1] = spieler2;
                res[2] = spieler3;
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
        // TODO
        TextView tv = (TextView) findViewById(R.id.tvSpielAuswertung);
        CheckBox cbKreuz = (CheckBox) findViewById(R.id.cbKreuz);
        CheckBox cbPik = (CheckBox) findViewById(R.id.cbPik);
        CheckBox cbHerz = (CheckBox) findViewById(R.id.cbHerz);
        CheckBox cbKaro = (CheckBox) findViewById(R.id.cbKaro);
        EditText edPunkteRe = (EditText) findViewById(R.id.edPunkteRe);

        schwarz_gespielt = (cbSchwarzGespielt.isChecked() ? 1 : 0);

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
        else
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

        punkte_re = readIntOrZero(edPunkteRe);

        if (schwarz_angesagt > 0) {
            if (ouvert > 0) {
                s += " Ouvert";
                multiplikator += 4;
            } else {
                multiplikator += 2;
                s += " Schwarz " +multiplikator;
                multiplikator += 1;
                s += ", angesagt " +multiplikator;
            }

            if (schwarz_gespielt > 0) {
                schneider_gespielt = 1;
                solist_gewonnen = 1;
                multiplikator += 2;
            } else {
                schneider_gespielt = (punkte_re > 90) ? 1 : 0;
                solist_gewonnen = 0;
                multiplikator *= 2;
                s += ", VERLOREN";
            }
        } else if (schneider_angesagt > 0) {
            multiplikator += 1;
            s += ", Hand " +multiplikator;
            multiplikator += 1;
            s += ", Schneider " +multiplikator;
            multiplikator += 1;
            s += ", angesagt " +multiplikator;

            if (punkte_re > 90) {
                schneider_gespielt = 1;
                solist_gewonnen = 1;
                if (schwarz_gespielt > 0) {
                    schneider_gespielt = 1;
                    multiplikator += 1;
                    s += ", schwarz " +multiplikator;
                }
            } else {
                solist_gewonnen = 0;
                if (schwarz_gespielt > 0) {
                    schneider_gespielt = 1;
                    multiplikator += 1;
                    s += ", Schwarz " +multiplikator;
                    schneider_gespielt = (punkte_re <= 30) ? 1 : 0;
                }
                multiplikator *= 2;
                s += ", VERLOREN " +multiplikator;
            }
        } else {
            if (handspiel > 0) {
                s += " Hand";
                multiplikator += 1;
            }

            // Normale Auswertung
            if (schwarz_gespielt > 0) {
                schneider_gespielt = 1;
                multiplikator += 2;
                s += ", Schwarz " +multiplikator;
            } else if ( (punkte_re > 90) || (punkte_re <= 30) ) {
                schneider_gespielt = 1;
                multiplikator += 1;
                s += ", Schneider " +multiplikator;
            } else {
                schneider_gespielt = 0;
            }

            if (punkte_re > 60) {
                solist_gewonnen = 1;
            } else {
                solist_gewonnen = 0;
                multiplikator *= 2;
                s += ", VERLOREN " +multiplikator;
            }
        }

        if ((solist_gewonnen > 0) && (multiplikator * grundwert < reizwert)) {
            solist_gewonnen = 0;
            multiplikator *= 2;
            s += ", VERLOREN (ÜBERREIZT) " +multiplikator;
        }
        if (kontra > 0) {
            multiplikator *= 2;
            s += ", Kontra " +multiplikator;
        }
        if (re > 0) {
            multiplikator *= 2;
            s += ", Re " +multiplikator;
        }
        if (bock > 0) {
            multiplikator *= 2;
            s += ", Bock " +multiplikator;
        }

        spielwert = grundwert * multiplikator;
        s += " x "
                +spiel +" (" +grundwert +")"
                +" = " +spielwert;

        tv.setText(s);
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

        ramschVerliererCount = 0;
        for (int i = 0; i < 3; i++) {
            if (ramschPunkte[i] == max) {
                // Spieler hat verloren
                ramschVerliererCount++;

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
            s += ", Kontra";
        }
        if (re > 0) {
            spielwert *=2;
            s += "Re";
        }
        if (bock > 0) {
            spielwert *= 2;
            s += ", Bock";
        }
        if (solist_gewonnen > 0) {
            spielwert = grundwert;
        } else{
            spielwert = 2 * grundwert;
            s += ", VERLOREN";
        }

        s += " = " +spielwert;
        tv.setText(s);
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

            boolean bockUndRamsch = false;
            final int spielwertRaw = spielwert /
                    ((solist_gewonnen == 0) ? 2 : 1) /
                    (kontra > 0 ? 2 : 1) /
                    (re > 0 ? 2 : 1) /
                    (bock > 0 ? 2 : 1);
            if ((schneider_angesagt == 0 && punkte_re == 60) ||
                    (spielwertRaw >= 100) ||
                    (kontra > 0 && solist_gewonnen == 0) ||
                    (spiel.equals("Ramsch") && ramschVerliererCount > 1) ) {
                bockUndRamsch = true;
            }

            Intent main = new Intent(AddSkatrundeActivity.this, SkatListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.putExtra("bockUndRamsch", bockUndRamsch);
            main.putExtra("warRamsch", spiel.equals("Ramsch"));
            main.putExtra("intentFlag", SkatListActivity.INTENT_FLAG_RUNDE_EINGETRAGEN);
            startActivity(main);
        }

        return super.onOptionsItemSelected(item);
    }

    private int readIntOrZero(EditText ed) {
        return readIntOrZero(ed.getText().toString());
    }

    private int readIntOrZero(String s) {
        int res = 0;
        try {
            res = Integer.valueOf(s);
        } catch (Exception e) {
            // Do nothing
        }
        return res;
    }
    */
}
