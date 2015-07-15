package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        calculateStats();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuExport) {
            DBController dbCon = new DBController(this);
            dbCon.open();
            final Cursor cursor = dbCon.fetch();
            new Thread(new Task(cursor, null)).start();
            Toast.makeText(getApplicationContext(), "Excel-Datei auf SD-Karte erstellt.", Toast.LENGTH_SHORT).show();
            dbCon.close();
        }

        return super.onOptionsItemSelected(item);
    }

    public void neuesSpiel(View view) {
        Intent intent = new Intent(getApplicationContext(), NeuesSpielActivity.class);
        startActivity(intent);
    }

    public void spielFortsetzen(View view) {
        SharedPreferences prefs = getSharedPreferences("skatPrefs", MODE_PRIVATE);
        String datum = prefs.getString("lastRound", null);
        if (datum == null) {
            Toast.makeText(getApplicationContext(), "Kein Spiel gefunden. Neues Spiel starten.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), SkatListActivity.class);
            intent.putExtra("intentFlag", SkatListActivity.INTENT_FLAG_SPIEL_FORTSETZEN);
            intent.putExtra("datum", datum);
            startActivity(intent);
        }
    }

    public void spielLaden(View view) {
        Intent intent = new Intent(getApplicationContext(), LoadGameActivity.class);
        startActivity(intent);
    }

    private void calculateStats() {
        DBController dbCon = new DBController(this);
        dbCon.open();
        TextView tvStats = (TextView) findViewById(R.id.tvStats);

        String totalRounds =  "" +dbCon.getTotalGamesCount();
        tvStats.append("\n");
        tvStats.append("\nGespielte Runden: " +totalRounds);
        String mostValuableRound = dbCon.getMostValuableRound();
        tvStats.append("\nBeste Runde: " +mostValuableRound);
    }
}
