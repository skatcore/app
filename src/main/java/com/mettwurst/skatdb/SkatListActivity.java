package com.mettwurst.skatdb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SkatListActivity extends ActivityWithSkatinfo {
    private static String datum;
    private static String spieler1;
    private static String spieler2;
    private static String spieler3;
    private static String spieler4;
    private static String spieler5;
    private static int spielerzahl;
    private static int geber;

    public static final int INTENT_FLAG_DEFAULT             = 0;
    public static final int INTENT_FLAG_NEUES_SPIEL         = 1;
    public static final int INTENT_FLAG_RUNDE_EINGETRAGEN   = 2;
    public static final int INTENT_FLAG_SPIEL_FORTSETZEN    = 3;

    private DBController dbCon;
    private ListView listView;

    private static int bockRamschStatus; // 0: Normal, 1: Bock, 2: Ramsch
    private static int bockCount;
    private static int ramschCount;

    private static int oldBockRamschStatus = 0;
    private static int oldBockCount = 0;
    private static int oldRamschCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skat_list);
        setTitle("Skatliste");

        dbCon = new DBController(this);
        dbCon.open();

        Intent intent = getIntent();
        int intentFlag = intent.getIntExtra("intentFlag", INTENT_FLAG_DEFAULT);
        switch (intentFlag) {
            case INTENT_FLAG_NEUES_SPIEL:
                datum = intent.getStringExtra("datum");
                spieler1 = intent.getStringExtra("spieler1");
                spieler2 = intent.getStringExtra("spieler2");
                spieler3 = intent.getStringExtra("spieler3");
                spieler4 = intent.getStringExtra("spieler4");
                spieler5 = intent.getStringExtra("spieler5");
                spielerzahl = intent.getIntExtra("spielerzahl", -1);
                geber = 1;
                bockRamschStatus = 0;
                bockCount = 0;
                ramschCount = 0;

                DBSpielController dbSpielCon = new DBSpielController(this);
                dbSpielCon.open();
                dbSpielCon.insert(datum, new String[]{spieler1, spieler2, spieler3, spieler4, spieler5});
                dbSpielCon.close();
                setLastPlayedRound(datum);
                break;
            case INTENT_FLAG_RUNDE_EINGETRAGEN:
                oldBockCount = bockCount; // Backup for last round deletion
                oldBockRamschStatus = bockRamschStatus;
                oldRamschCount = ramschCount;

                boolean bockUndRamsch = intent.getBooleanExtra("bockUndRamsch", false);
                boolean warRamsch = intent.getBooleanExtra("warRamsch", false);
                switch (bockRamschStatus) {
                    case 0:
                        if (bockUndRamsch) {
                            bockCount += spielerzahl;
                            ramschCount += spielerzahl;
                            bockRamschStatus = 1;
                        }
                        break;
                    case 1:
                        bockCount -= 1;
                        if (bockCount % spielerzahl == 0) {
                            bockRamschStatus = 2;
                        }
                        break;
                    case 2:
                        if (warRamsch) {
                            // Bei Grand Hand nicht reduzieren
                            ramschCount -= 1;
                            if (ramschCount % spielerzahl == 0) {
                                bockRamschStatus = 0;
                            }
                        }
                        geber -= 1; // Grand Hand anstelle von Ramsch
                        break;
                }
                geber = (geber % spielerzahl) + 1;
                break;
            case INTENT_FLAG_SPIEL_FORTSETZEN:
                datum = intent.getStringExtra("datum");
                try {
                    Cursor cursor = dbCon.fetchForDate(datum);
                    cursor.moveToLast();
                    spielerzahl = cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COL_SPIELERZAHL));
                    spieler1 = cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER1));
                    spieler2 = cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER2));
                    spieler3 = cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER3));
                    spieler4 = cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER4));
                    if (spieler4 == null) spieler4 = "";
                    spieler5 = cursor.getString(cursor.getColumnIndex(DBContract.Entry.COL_SPIELER5));
                    if (spieler5 == null) spieler5 = "";
                    geber = cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COL_GEBER));
                    geber = (geber % spielerzahl) + 1;
                    bockRamschStatus = cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COL_BOCK_RAMSCH_STATUS));
                    bockCount = cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COL_BOCK_COUNT));
                    ramschCount = cursor.getInt(cursor.getColumnIndex(DBContract.Entry.COL_RAMSCH_COUNT));
                    setLastPlayedRound(datum);
                } catch (Exception e) {
                    String spielerstring = intent.getStringExtra("spieler");
                    if (spielerstring == null) {
                        Toast.makeText(getApplicationContext(), "Keine Daten vorhanden. Neues Spiel erstellen oder laden.", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intentMain);
                    } else {
                        Toast.makeText(getApplicationContext(), "Keine Runde eingetragen, erstelle neues Spiel.", Toast.LENGTH_SHORT).show();
                        String[] spieler = spielerstring.split(", ");
                        spielerzahl = spieler.length;
                        spieler1 = spieler[0];
                        spieler2 = spieler[1];
                        spieler3 = spieler[2];
                        if (spielerzahl >= 4) {
                            spieler4 = spieler[3];
                        } else {
                            spieler4 = "";
                        }
                        if (spielerzahl >= 5) {
                            spieler5 = spieler[4];
                        } else {
                            spieler5 = "";
                        }
                        geber = 1;
                        bockRamschStatus = 0;
                        bockCount = 0;
                        ramschCount = 0;
                        setLastPlayedRound(datum);
                    }
                }
                break;
            case INTENT_FLAG_DEFAULT:
                break;
        }

        // Header: Player names + "Spiel"
        LinearLayout llPlayernames = (LinearLayout) findViewById(R.id.llPlayernames);
        llPlayernames.setWeightSum(spielerzahl + 1);
        TextView tvPlayer1 = (TextView) findViewById(R.id.tvPlayer1);
        tvPlayer1.setText(spieler1);
        TextView tvPlayer2 = (TextView) findViewById(R.id.tvPlayer2);
        tvPlayer2.setText(spieler2);
        TextView tvPlayer3 = (TextView) findViewById(R.id.tvPlayer3);
        tvPlayer3.setText(spieler3);
        TextView tvPlayer4 = (TextView) findViewById(R.id.tvPlayer4);
        tvPlayer4.setText(spieler4);
        tvPlayer4.setVisibility((spielerzahl >= 4) ? View.VISIBLE : View.GONE);
        TextView tvPlayer5 = (TextView) findViewById(R.id.tvPlayer5);
        tvPlayer5.setText(spieler5);
        tvPlayer5.setVisibility((spielerzahl >= 5) ? View.VISIBLE : View.GONE);

        listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.textViewEmpty));

        updateListViewAndAdapter();

        // OnCLickListener For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                String id       = getString(view.findViewById(R.id.tvId));
                Intent intent = new Intent(getApplicationContext(), InspectSkatrundeActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }

            private String getString(View v) {
                TextView tv = (TextView) v;
                CharSequence charSeq = tv.getText();
                if (charSeq != null) {
                    return charSeq.toString();
                } else {
                    return "";
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.skat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuAdd) {
            Intent intent = new Intent(getApplicationContext(), AddAnsageActivity.class);
            intent.putExtra("datum",datum);
            intent.putExtra("spieler1",spieler1);
            intent.putExtra("spieler2",spieler2);
            intent.putExtra("spieler3",spieler3);
            intent.putExtra("spieler4",spieler4);
            intent.putExtra("spieler5",spieler5);
            intent.putExtra("spielerzahl",spielerzahl);
            intent.putExtra("geber",geber);
            intent.putExtra("bock",(bockRamschStatus == 1) ? 1 : 0);
            intent.putExtra("pflichtramsch",(bockRamschStatus == 2) ? 1 : 0);

            Cursor cursor = dbCon.fetchForDate(datum);
            cursor.moveToLast();
            int gesamt_sp1 = readIntOrZero(cursor, DBContract.Entry.COL_GESAMT_SP1);
            int gesamt_sp2 = readIntOrZero(cursor, DBContract.Entry.COL_GESAMT_SP2);
            int gesamt_sp3 = readIntOrZero(cursor, DBContract.Entry.COL_GESAMT_SP3);
            int gesamt_sp4 = readIntOrZero(cursor, DBContract.Entry.COL_GESAMT_SP4);
            int gesamt_sp5 = readIntOrZero(cursor, DBContract.Entry.COL_GESAMT_SP5);

            intent.putExtra("gesamt_sp1", gesamt_sp1);
            intent.putExtra("gesamt_sp2", gesamt_sp2);
            intent.putExtra("gesamt_sp3", gesamt_sp3);
            intent.putExtra("gesamt_sp4", gesamt_sp4);
            intent.putExtra("gesamt_sp5", gesamt_sp5);

            startActivity(intent);
        } else if (id == R.id.menuDeleteLastRound) {
            if (listView.getCount() == 0) {
                Toast.makeText(getApplication(), "Noch keine Runde eingetragen.", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setTitle("Löschen")
                        .setMessage("Letzte Runde löschen?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Yes pressed
                                dbCon.deleteLastGameForDate(datum);
                                updateListViewAndAdapter();
                                geber -= 1;
                                if (geber <= 0) {
                                    geber = spielerzahl;
                                }
                                ramschCount = oldRamschCount; // Restore backup
                                bockRamschStatus = oldBockRamschStatus;
                                bockCount = oldBockCount;
                            }
                        })
                        .setNegativeButton("Nein", null) //Do nothing on no
                        .show();
            }
        } else if (id == R.id.menuExport) {
            final Cursor cursor = dbCon.fetchForDate(datum);
            new Thread(new Task(cursor, datum)).start();
            Toast.makeText(getApplicationContext(), "Excel-Datei auf SD-Karte erstellt.", Toast.LENGTH_SHORT).show();
        }

    return super.onOptionsItemSelected(item);
            }

    private int readIntOrZero(Cursor cursor, String column) {
            int res = 0;
            try {
                res = Integer.valueOf(cursor.getString(cursor.getColumnIndex(column)));
            } catch (Exception e) {
                // Do nothing, res stays 0
            }
            return res;
    }

    // Attaches the data from database into ListView using Cursor Adapter
    private void updateListViewAndAdapter() {
        Cursor cursor = dbCon.fetchForDate(datum); // Filtering for current date (game)
        String[] from = new String[]{
                DBContract.Entry._ID,
                DBContract.Entry.COL_GESAMT_SP1,
                DBContract.Entry.COL_GESAMT_SP2,
                DBContract.Entry.COL_GESAMT_SP3,
                DBContract.Entry.COL_GESAMT_SP4,
                DBContract.Entry.COL_GESAMT_SP5,
                DBContract.Entry.COL_SPIELWERT};

        int[] into = new int[]{
                R.id.tvId,
                R.id.tvSpieler1,
                R.id.tvSpieler2,
                R.id.tvSpieler3,
                R.id.tvSpieler4,
                R.id.tvSpieler5,
                R.id.tvSpiel};

        int layoutId;
        if (spielerzahl >= 5) {
            layoutId = R.layout.activity_view_record_5;
        } else if (spielerzahl >= 4) {
            layoutId = R.layout.activity_view_record_4;
        } else {
            layoutId = R.layout.activity_view_record_3;
        }
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(SkatListActivity.this, layoutId, cursor, from, into, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private void setLastPlayedRound(String date) {
        SharedPreferences.Editor editor = getSharedPreferences("skatPrefs", MODE_PRIVATE).edit();
        editor.putString("lastRound", date);
        editor.apply();
    }
}