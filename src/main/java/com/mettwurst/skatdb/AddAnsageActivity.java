package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class AddAnsageActivity extends Activity{
    private String datum;
    private String spieler1;
    private String spieler2;
    private String spieler3;
    private String spieler4;
    private String spieler5;
    private int spielerzahl;
    private int geber;
    private int bock;

    private static String solist;
    private static int handspiel;
    private static int ouvert;
    private static int schneider_angesagt;
    private static int schwarz_angesagt;
    private static int reizwert;
    private static String spiel;
    private static int kontra;
    private static int re;
    private static int jungfrau_angesagt1;
    private static int jungfrau_angesagt2;
    private static int jungfrau_angesagt3;
    private static int jungfrau_angesagt4;
    private static int jungfrau_angesagt5;

    private boolean pflichtramsch;
    private int gesamt_sp1;
    private int gesamt_sp2;
    private int gesamt_sp3;
    private int gesamt_sp4;
    private int gesamt_sp5;

    private String[] playingPlayersInOrder;

    private LinearLayout llSolist;
        private RadioGroup rgSolist;
            private RadioButton rbSolist1;
            private RadioButton rbSolist2;
            private RadioButton rbSolist3;
    private RadioGroup rgGrandRamschNull;
        private RadioButton rbGrand;
        private RadioButton rbRamsch;
        private RadioButton rbNull;
        private RadioButton rbDummy1;
    private RadioGroup rgFarbspiele;
        private RadioButton rbKreuz;
        private RadioButton rbPik;
        private RadioButton rbHerz;
        private RadioButton rbKaro;
        private RadioButton rbDummy2;
    private TextView tvAnsagen;
    private LinearLayout llHandOuvertAnsagen;
        private CheckBox cbHandspiel;
        private CheckBox cbOuvert;
    private LinearLayout llSchneiderSchwarz;
        private CheckBox cbSchneiderAngesagt;
        private CheckBox cbSchwarzAngesagt;
    private LinearLayout llKontraRe;
        private CheckBox cbKontra;
        private CheckBox cbRe;
    private LinearLayout llJungfrauAnsagen;
        private CheckBox cbJungfrauAnsage1;
        private CheckBox cbJungfrauAnsage2;
        private CheckBox cbJungfrauAnsage3;
    private LinearLayout llReizwert;
        private NumberPicker npReizwert;
    private TextView tvAufspiel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read all collected information delivered via Intent
        Intent intent = getIntent();

        // From SkatListActivity
        datum       = intent.getStringExtra("datum");
        spieler1    = intent.getStringExtra("spieler1");
        spieler2    = intent.getStringExtra("spieler2");
        spieler3    = intent.getStringExtra("spieler3");
        spieler4    = intent.getStringExtra("spieler4");
        spieler5    = intent.getStringExtra("spieler5");
        spielerzahl = intent.getIntExtra("spielerzahl", -1);
        geber       = intent.getIntExtra("geber", -1);
        bock        = intent.getIntExtra("bock", 0);

        pflichtramsch = intent.getIntExtra("pflichtramsch", 0) > 0;

        // Current total points. We just pass them to the next activity
        gesamt_sp1 = intent.getIntExtra("gesamt_sp1", 0);
        gesamt_sp2 = intent.getIntExtra("gesamt_sp2", 0);
        gesamt_sp3 = intent.getIntExtra("gesamt_sp3", 0);
        gesamt_sp4 = intent.getIntExtra("gesamt_sp4", 0);
        gesamt_sp5 = intent.getIntExtra("gesamt_sp5", 0);

        setContentView(R.layout.activity_add_ansage);
        // Store views for later access
        rgSolist            = (RadioGroup) findViewById(R.id.rgSolist);
        rbSolist1           = (RadioButton) findViewById(R.id.rbSolist1);
        rbSolist2           = (RadioButton) findViewById(R.id.rbSolist2);
        rbSolist3           = (RadioButton) findViewById(R.id.rbSolist3);
        rgGrandRamschNull   = (RadioGroup) findViewById(R.id.rgGrandRamschNull);
        rbGrand             = (RadioButton) findViewById(R.id.rbGrand);
        rbRamsch            = (RadioButton) findViewById(R.id.rbRamsch);
        rbNull              = (RadioButton) findViewById(R.id.rbNull);
        rbDummy1            = (RadioButton) findViewById(R.id.rbDummy1);
        rgFarbspiele        = (RadioGroup) findViewById(R.id.rgFarbspiele);
        rbKreuz             = (RadioButton) findViewById(R.id.rbKreuz);
        rbPik               = (RadioButton) findViewById(R.id.rbPik);
        rbHerz              = (RadioButton) findViewById(R.id.rbHerz);
        rbKaro              = (RadioButton) findViewById(R.id.rbKaro);
        rbDummy2            = (RadioButton) findViewById(R.id.rbDummy2);
        cbHandspiel         = (CheckBox) findViewById(R.id.cbHandspiel);
        cbOuvert            = (CheckBox) findViewById(R.id.cbOuvert);
        cbSchneiderAngesagt = (CheckBox) findViewById(R.id.cbSchneiderAngesagt);
        cbSchwarzAngesagt   = (CheckBox) findViewById(R.id.cbSchwarzAngesagt);
        cbKontra            = (CheckBox) findViewById(R.id.cbKontra);
        cbRe                = (CheckBox) findViewById(R.id.cbRe);
        npReizwert          = (NumberPicker) findViewById(R.id.npReizwert);
        tvAufspiel          = (TextView) findViewById(R.id.tvAufspiel);
        llSolist            = (LinearLayout) findViewById(R.id.llSolist);
        llHandOuvertAnsagen = (LinearLayout) findViewById(R.id.llHandOuvertAnsagen);
        llSchneiderSchwarz  = (LinearLayout) findViewById(R.id.llSchneiderSchwarz);
        llKontraRe          = (LinearLayout) findViewById(R.id.llKontraRe);
        llJungfrauAnsagen   = (LinearLayout) findViewById(R.id.llJungfrauAnsagen);
        llReizwert          = (LinearLayout) findViewById(R.id.llReizwert);
        cbJungfrauAnsage1   = (CheckBox) findViewById(R.id.cbJungfrauAnsage1);
        cbJungfrauAnsage2   = (CheckBox) findViewById(R.id.cbJungfrauAnsage2);
        cbJungfrauAnsage3   = (CheckBox) findViewById(R.id.cbJungfrauAnsage3);
        tvAnsagen           = (TextView) findViewById(R.id.tvAnsagen);

        // Namen der Solisten eintragen
        playingPlayersInOrder = AddSkatrundeActivity.getPlayingPlayersInOrder(geber, spielerzahl, spieler1, spieler2, spieler3, spieler4, spieler5);
        rbSolist1.setText(playingPlayersInOrder[0]);
        rbSolist2.setText(playingPlayersInOrder[1]);
        rbSolist3.setText(playingPlayersInOrder[2]);

        rgGrandRamschNull.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != R.id.rbDummy1) {
                    rbDummy2.performClick();
                }

                if (checkedId == R.id.rbRamsch) {
                    setRamsch();
                } else {
                    setNormalOrGrandHand();
                }
            }
        });

        rgFarbspiele.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != R.id.rbDummy2) {
                    rbDummy1.performClick();
                }

                if (checkedId == R.id.rbRamsch) {
                    setRamsch();
                } else {
                    setNormalOrGrandHand();
                }
            }
        });

        // Automatische Anpassung der Ansagen (etwa bei Schwarz auch Schneider ansagen)
        CompoundButton.OnCheckedChangeListener cbListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean keinNull = !spiel.equals("Null");
                switch (buttonView.getId()) {
                    case R.id.cbHandspiel:
                        if (!isChecked) {
                            cbSchneiderAngesagt.setChecked(false);
                            cbSchwarzAngesagt.setChecked(false);
                            if (keinNull) cbOuvert.setChecked(false);
                        }
                        break;
                    case R.id.cbOuvert:
                        if (isChecked) {
                            if (keinNull) cbHandspiel.setChecked(true);
                            cbSchneiderAngesagt.setChecked(true);
                            cbSchwarzAngesagt.setChecked(true);
                        }
                        break;
                    case R.id.cbSchneiderAngesagt:
                        if (isChecked) {
                            if (keinNull) cbHandspiel.setChecked(true);
                        } else {
                            cbSchwarzAngesagt.setChecked(false);
                            if (keinNull) cbOuvert.setChecked(false);
                        }
                        break;
                    case R.id.cbSchwarzAngesagt:
                        if (isChecked) {
                            if (keinNull) cbHandspiel.setChecked(true);
                            cbSchneiderAngesagt.setChecked(true);
                        } else {
                            if (keinNull) cbOuvert.setChecked(false);
                        }
                        break;
                    case R.id.cbKontra:
                        if (!isChecked) cbRe.setChecked(false);
                        break;
                    case R.id.cbRe:
                        if (isChecked) cbKontra.setChecked(true);
                        break;
                }
            }
        };
        cbHandspiel.setOnCheckedChangeListener(cbListener);
        cbOuvert.setOnCheckedChangeListener(cbListener);
        cbSchneiderAngesagt.setOnCheckedChangeListener(cbListener);
        cbSchwarzAngesagt.setOnCheckedChangeListener(cbListener);
        cbKontra.setOnCheckedChangeListener(cbListener);
        cbRe.setOnCheckedChangeListener(cbListener);

        final String[] reizwerteStr =
                            {"18","20","22","23","24","27","30","33","35","36",
                            "40","44","45","46","48","50","54","55","59","60",
                            "63","66","70","72","77","80","81","84","88","90",
                            "96","99","100","108","110","117","120","121","126","130",
                            "132","135","140","143","144","150","153","154","156","160",
                            "162","165","168","170","176","180","187","198","198","204",
                            "216","240","264"};

        npReizwert.setMinValue(0);
        npReizwert.setMaxValue(reizwerteStr.length - 1);
        npReizwert.setDisplayedValues(reizwerteStr);
        npReizwert.setWrapSelectorWheel(false);
        npReizwert.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        cbJungfrauAnsage1.setText(playingPlayersInOrder[0]);
        cbJungfrauAnsage2.setText(playingPlayersInOrder[1]);
        cbJungfrauAnsage3.setText(playingPlayersInOrder[2]);

        // Spieler nach dem Geber spielt auf
        final String aufspiel;
        switch (geber) {
            case 1:
                aufspiel = spieler2;
                break;
            case 2:
                aufspiel = spieler3;
                break;
            case 3:
                aufspiel = (spielerzahl >= 4) ? spieler4 : spieler1;
                break;
            case 4:
                aufspiel = (spielerzahl >= 5) ? spieler5 : spieler1;
                break;
            default: // 5
                aufspiel = spieler1;
                break;
        }
        tvAufspiel.setText("Aufspiel: " +aufspiel);

        if (pflichtramsch) {
            setPflichtramsch();
            rbRamsch.callOnClick();
        } else {
            // Irgendwas als Voreinstellung nehmen, damit Layout generiert wird.
            rbKreuz.performClick();
        }

        /*
        // DEBUG: Clear all rows from table
        DBController dbCon = new DBController(this);
        dbCon.open();
        dbCon.clearTable();
        */
    }

    // Passt Layout und Auswahlmoeglichkeiten an Pflichramsch an
    private void setPflichtramsch() {
        rbNull.setVisibility(View.GONE);
        rbKreuz.setVisibility(View.GONE);
        rbPik.setVisibility(View.GONE);
        rbHerz.setVisibility(View.GONE);
        rbKaro.setVisibility(View.GONE);
        cbOuvert.setVisibility(View.GONE);

        llSchneiderSchwarz.setVisibility(View.GONE);
        llKontraRe.setVisibility(View.GONE);
        llJungfrauAnsagen.setVisibility(View.VISIBLE);
        llReizwert.setVisibility(View.GONE);
    }

    private void setNormalOrGrandHand() {
        llSolist.setVisibility(View.VISIBLE);
        llHandOuvertAnsagen.setVisibility(View.VISIBLE);
        if (!pflichtramsch) {
            if (rbNull.isChecked()) {
                llSchneiderSchwarz.setVisibility(View.GONE);
            } else {
                llSchneiderSchwarz.setVisibility(View.VISIBLE);
            }
            llKontraRe.setVisibility(View.VISIBLE);
            llReizwert.setVisibility(View.VISIBLE);
        }
        llJungfrauAnsagen.setVisibility(View.GONE);

        switch(rgSolist.getCheckedRadioButtonId()) {
            case R.id.rbSolist1:
                solist = playingPlayersInOrder[0];
                break;
            case R.id.rbSolist2:
                solist = playingPlayersInOrder[1];
                break;
            case R.id.rbSolist3:
                solist = playingPlayersInOrder[2];
                break;
        }

        if (pflichtramsch) {
            cbHandspiel.setChecked(true);
        }

        handspiel = cbHandspiel.isChecked() ? 1 : 0;
        ouvert = cbOuvert.isChecked() ? 1 : 0;
        schneider_angesagt = cbSchneiderAngesagt.isChecked() ? 1 : 0;
        schwarz_angesagt = cbSchwarzAngesagt.isChecked() ? 1 : 0;

        if (rbGrand.isChecked()) {
            spiel = "Grand";
        } else if (rbNull.isChecked()) {
            spiel = "Null";
        } else if (rbKreuz.isChecked()) {
            spiel = "Kreuz";
        } else if (rbPik.isChecked()) {
            spiel = "Pik";
        } else if (rbHerz.isChecked()) {
            spiel = "Herz";
        } else if (rbKaro.isChecked()) {
            spiel = "Karo";
        }
        setTitle(spiel);
        tvAnsagen.setText("Ansagen");

        kontra = cbKontra.isChecked() ? 1 : 0;
        re = cbRe.isChecked() ? 1 : 0;
        jungfrau_angesagt1 = 0;
        jungfrau_angesagt2 = 0;
        jungfrau_angesagt3 = 0;
        jungfrau_angesagt4 = 0;
        jungfrau_angesagt5 = 0;
    }

    private void setRamsch() {
        solist = "";
        handspiel = 0;
        ouvert = 0;
        schneider_angesagt = 0;
        schwarz_angesagt = 0;
        spiel = "Ramsch";
        kontra = 0;
        re = 0;

        setTitle(spiel);
        llSolist.setVisibility(View.GONE);
        llHandOuvertAnsagen.setVisibility(View.GONE);
        llSchneiderSchwarz.setVisibility(View.GONE);
        llKontraRe.setVisibility(View.GONE);
        tvAnsagen.setText("Jungfrau angesagt");
        llJungfrauAnsagen.setVisibility(View.VISIBLE);
        cbJungfrauAnsage1.setText(playingPlayersInOrder[0]);
        cbJungfrauAnsage2.setText(playingPlayersInOrder[1]);
        cbJungfrauAnsage3.setText(playingPlayersInOrder[2]);
        llReizwert.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_ansage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menuAddAnsage) {
            if (spiel.equals("Ramsch") || pflichtramsch) {
                setRamsch();
                reizwert = 0;
            } else {
                setNormalOrGrandHand();
                final int[] reizwerteInt =
                        {18, 20, 22, 23, 24, 27, 30, 33, 35, 36, 40, 44, 45, 46, 48, 50, 54, 55,
                                59, 60, 63, 66, 70, 72, 77, 80, 81, 84, 88, 90, 96, 99, 100, 108, 110,
                                117, 120, 121, 126, 130, 132, 135, 140, 143, 144, 150, 153, 154, 156,
                                160, 162, 165, 168, 170, 176, 180, 187, 198, 198, 204, 216, 240, 264};
                reizwert = reizwerteInt[npReizwert.getValue()];
            }

            Intent intent = new Intent(getApplicationContext(), AddSkatrundeActivity.class);
            intent.putExtra("datum",datum);
            intent.putExtra("spieler1",spieler1);
            intent.putExtra("spieler2",spieler2);
            intent.putExtra("spieler3",spieler3);
            intent.putExtra("spieler4",spieler4);
            intent.putExtra("spieler5",spieler5);
            intent.putExtra("spielerzahl",spielerzahl);
            intent.putExtra("geber",geber);
            intent.putExtra("bock",bock);

            intent.putExtra("solist",solist);
            intent.putExtra("handspiel",handspiel);
            intent.putExtra("ouvert",ouvert);
            intent.putExtra("schneider_angesagt",schneider_angesagt);
            intent.putExtra("schwarz_angesagt",schwarz_angesagt);
            intent.putExtra("reizwert",reizwert);
            intent.putExtra("spiel",spiel);
            intent.putExtra("kontra",kontra);
            intent.putExtra("re",re);
            intent.putExtra("jungfrau_angesagt1",jungfrau_angesagt1);
            intent.putExtra("jungfrau_angesagt2",jungfrau_angesagt2);
            intent.putExtra("jungfrau_angesagt3",jungfrau_angesagt3);
            intent.putExtra("jungfrau_angesagt4",jungfrau_angesagt4);
            intent.putExtra("jungfrau_angesagt5",jungfrau_angesagt5);

            intent.putExtra("gesamt_sp1", gesamt_sp1);
            intent.putExtra("gesamt_sp2", gesamt_sp2);
            intent.putExtra("gesamt_sp3", gesamt_sp3);
            intent.putExtra("gesamt_sp4", gesamt_sp4);
            intent.putExtra("gesamt_sp5", gesamt_sp5);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
