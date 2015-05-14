package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/** TODO
    - bockUndRamsch-Feedback lesen und anwenden.
    - Runden löschen (dabei Gesamtpunkte beachten!).
    - Runden bearbeiten (-----"-----).

    Bugs
    - Wenn man ein Spiel laedt, wird nicht erkannt, ob Pflichtramsch war.
 */


public class SkatListActivity extends Activity {
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

    private static int bock_count = 0;
    private static int pflichtramsch_count = 0;
    private int bockRamschStatus = 0; // 0: Normal, 1: Bock, 2: Ramsch

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
                pflichtramsch_count = 0;
                bockRamschStatus = 0;
                break;
            case INTENT_FLAG_RUNDE_EINGETRAGEN:
                boolean bockUndRamsch = intent.getBooleanExtra("bockUndRamsch", false);
                boolean warRamsch = intent.getBooleanExtra("warRamsch", false);
                switch (bockRamschStatus) {
                    case 0:
                        if (bockUndRamsch) {
                            bock_count += spielerzahl;
                            pflichtramsch_count += spielerzahl;
                            bockRamschStatus = 1;
                        }
                        break;
                    case 1:
                        bock_count -= 1;
                        if (bock_count % spielerzahl == 0) {
                            bockRamschStatus = 2;
                        }
                        break;
                    case 2:
                        if (warRamsch) {
                            // Bei Grand Hand nicht reduzieren
                            pflichtramsch_count -= 1;
                            if (pflichtramsch_count % spielerzahl == 0) {
                                bockRamschStatus = 0;
                            }
                        }
                        geber -= 1; // Grand Hand anstelle von Ramsch
                        break;
                }
                geber = (geber % spielerzahl) + 1;
                break;
            case INTENT_FLAG_SPIEL_FORTSETZEN:
                // TODO
                datum = intent.getStringExtra("datum");
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
                pflichtramsch_count = 0;    // Annahme:
                bockRamschStatus = 0;       // Bock und Ramsch wurde zu Ende gepsielt.
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

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.textViewEmpty));
        // Attach the data from database into ListView using Cursor Adapter
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
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, layoutId, cursor, from, into, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

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

            /**
             Log.e("datum:", datum);
             Log.e("sp1:", spieler1);
             Log.e("sp2:", spieler2);
             Log.e("sp3:", spieler3);
             Log.e("sp4:", spieler4);
             Log.e("sp5:", spieler5);
             Log.e("spielerzahl:", "" +spielerzahl);
             Log.e("geber:", "" +geber);
             Log.e("bock:", "" +((bockRamschStatus == 1) ? 1 : 0));
             Log.e("pflichramsch:", "" +((bockRamschStatus == 2) ? 1 : 0));
             */

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
        } else if(id == R.id.menuExport) {
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
}
// TODO: this is just a test. (Github)