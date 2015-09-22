package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import javax.security.auth.login.LoginException;

public class X7Auswertung extends Activity {

    private SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
    private int spielerzahl = infoSingleton.spielerzahl;  // Local copy
    private int geber = infoSingleton.geber;              //
    int mySolistGewonnen = infoSingleton.solist_gewonnen; //
    private int spielwert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x7_auswertung);

        String s = "";

        SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();

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
                // TODO: Daten in Singleton eintragen
                break;

            default:
                switch (infoSingleton.spiel) {
                    case "Grand":
                        spielwert = 24;
                        break;
                    case "Kreuz":
                        spielwert = 12;
                        break;
                    case "Pik":
                        spielwert = 11;
                        break;
                    case "Herz":
                        spielwert = 10;
                        break;
                    case "Karo":
                        spielwert = 9;
                        break;
                }

                s += "Mit / Ohne " +(infoSingleton.buben_multiplikator-1) +" " +infoSingleton.spiel + " (" + spielwert + ")" ; // z.B.  Mit / Ohne 2 Kreuz (12)


                int multiplikator = infoSingleton.buben_multiplikator;

                if (infoSingleton.ouvert == 1) {
                    ////////// OUVERT //////////
                    multiplikator += 4;
                    if (infoSingleton.solist_gewonnen == 1) {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nOuvert, Schwarz x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            s += "\nOuvert x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nNicht Schwarz, VERLOREN x-2";
                        }else {
                            s += "\nOuvert x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nNicht Schwarz, VERLOREN x-2";
                        }
                    } else {
                        s += "\nOuvert x" + multiplikator;
                        mySolistGewonnen = 0;
                        multiplikator *= 2;
                        s += "\nVERLOREN x-2";
                    }
                } else if (infoSingleton.schwarz_angesagt == 1) {
                    ////////// HAND, SCHNEIDER SCHWARZ ANGESAGT //////////
                    multiplikator += 3;
                    if (infoSingleton.solist_gewonnen == 1) {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nSchwarz angesagt, Schwarz x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            s += "\nSchwarz angesagt x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nNicht Schwarz, VERLOREN x-2";
                        }else {
                            s += "\nSchwarz angesagt x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nNicht Schwarz, VERLOREN x-2";
                        }
                    } else {
                        s += "\nSchwarz angesagt x" + multiplikator;
                        mySolistGewonnen = 0;
                        multiplikator *= 2;
                        s += "\nVERLOREN x-2";
                    }
                } else if (infoSingleton.schneider_angesagt == 1) {
                    ////////// HAND, SCHNEIDER ANGESAGT //////////
                    multiplikator += 2;
                    if (infoSingleton.solist_gewonnen == 1) {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nHand, Schneider angesagt, Schwarz x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            multiplikator += 1;
                            s += "\nHand, Schneider angesagt x" + multiplikator;
                        } else {
                            s += "\nHand, Schneider angesagt x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nKein Schneider, VERLOREN x-2";
                        }
                    } else {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 1;
                            s += "\nHand, Schneider angesagt, Selber Schwarz x" + multiplikator;
                            mySolistGewonnen= 0;
                            multiplikator *= 2;
                            s += "\nVERLOREN x-2";
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            s += "\nHand, Schneider angesagt x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nVERLOREN x-2";
                        } else {
                            s += "\nHand, Schneider angesagt x" + multiplikator;
                            mySolistGewonnen = 0;
                            multiplikator *= 2;
                            s += "\nVERLOREN x-2";
                        }
                    }
                } else if (infoSingleton.handspiel == 1) {
                    ////////// HANDSPIEL //////////
                    multiplikator += 1;
                    if (infoSingleton.solist_gewonnen == 1) {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nHand, Schwarz gespielt x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            multiplikator += 1;
                            s += "\nHand, Schndeider gespielt x" + multiplikator;
                        } else {
                            s += "\nHand x" + multiplikator;
                        }
                    } else {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nHand, Selber Schwarz x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            multiplikator += 1;
                            s += "\nHand, Selber Schneider x" + multiplikator;
                        } else {
                            s += "\nHand x" + multiplikator;
                        }
                        mySolistGewonnen = 0;
                        multiplikator *= 2;
                        s += "\nVERLOREN x-2";
                    }
                } else {
                    ////////// NORMALES SPIEL //////////
                    if (infoSingleton.solist_gewonnen == 1) {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nSchwarz gespielt x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            multiplikator += 1;
                            s += "\nSchneider gespielt x" + multiplikator;
                        } else {
                            s += "\nSpiel " +multiplikator;
                        }
                    } else {
                        if (infoSingleton.schwarz_gespielt == 1) {
                            multiplikator += 2;
                            s += "\nSelber Schwarz x" + multiplikator;
                        } else if (infoSingleton.schneider_gespielt == 1) {
                            multiplikator += 1;
                            s += "\nSelber Schneider x" + multiplikator;
                        } else {
                            s += "\nSpiel " +multiplikator;
                        }
                        mySolistGewonnen = 0;
                        multiplikator *= 2;
                        s += "\nVERLOREN x-2";
                    }
                }
                spielwert *= multiplikator;
        }

        TextView tvAuswertung = (TextView) findViewById(R.id.tvAuswertung);
        tvAuswertung.setText(s);
        TextView tvGesamt = (TextView) findViewById(R.id.tvGesamt);
        tvGesamt.setText("Gesamt: " +((mySolistGewonnen == 1) ? "" : "-") +spielwert);
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

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_OK) {
            SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
            infoSingleton.spielwert = this.spielwert;
            infoSingleton.solist_gewonnen = mySolistGewonnen;

            int[] punkteSpieler = distributePointsToPlayers();
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

            Intent main = new Intent(X7Auswertung.this, SkatListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            main.putExtra("intentFlag", SkatListActivity.INTENT_FLAG_RUNDE_EINGETRAGEN);
            startActivity(main);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
        boolean isNullspiel = infoSingleton.spiel.equals("Null");

        Intent intent;
        if (isNullspiel) {
            intent = new Intent(getApplicationContext(), X5AuswertungAusgang.class);
        } else {
            intent = new Intent(getApplicationContext(), X6AuswertungSchneider.class);
        }
        startActivity(intent);
    }

    private int[] distributePointsToPlayers() {
        int solist = infoSingleton.solist;
        int solist_gewonnen = infoSingleton.solist_gewonnen;
        int[] res = new int[5];

        res[0] = (isPlayingSp1() &&
                (       ((solist == 1) && !(solist_gewonnen > 0)) ||  // Solist but lost game
                        (!(solist == 1) && (solist_gewonnen > 0))     // Not solist but solist won
                )) ? spielwert : 0;
        res[1] = (isPlayingSp2() &&
                (       ((solist == 2) && !(solist_gewonnen > 0)) ||
                        (!(solist == 2) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
        res[2] = (isPlayingSp3() &&
                (       ((solist ==3) && !(solist_gewonnen > 0)) ||
                        (!(solist == 3) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
        res[3] = (isPlayingSp4() &&
                (       ((solist == 4) && !(solist_gewonnen > 0)) ||
                        (!(solist == 4) && (solist_gewonnen > 0))
                )) ? spielwert : 0;
        res[4] = (isPlayingSp5() &&
                (       ((solist == 5) && !(solist_gewonnen > 0)) ||
                        (!(solist == 5) && (solist_gewonnen > 0))
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
