package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
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

        SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
        int mySolistGewonnen = infoSingleton.solist_gewonnen;
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
        if (id == R.id.action_settings) {
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
}
