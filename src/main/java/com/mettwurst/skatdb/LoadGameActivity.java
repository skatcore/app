package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.mettwurst.skatdb.DBSpielContract.Entry;


public class LoadGameActivity extends Activity {

    private DBSpielController dbSpielCon;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        setTitle("Spiel laden");

        dbSpielCon = new DBSpielController(this);
        dbSpielCon.open();

        listView = (ListView) findViewById(R.id.listViewGames);
        listView.setEmptyView(findViewById(R.id.textViewEmpty));

        Cursor cursor = dbSpielCon.fetch();
        String[] from = new String[]{
                Entry._ID,
                Entry.COL_DATUM,
                Entry.COL_SPIELER};

        int[] into = new int[]{
                R.id.tvId,
                R.id.tvSpielDatum,
                R.id.tvSpieler};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(LoadGameActivity.this, R.layout.activity_view_game_record, cursor, from, into, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        // OnCLickListener For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                String datum       = getString(view.findViewById(R.id.tvSpielDatum));
                Intent intent = new Intent(getApplicationContext(), SkatListActivity.class);
                intent.putExtra("intentFlag", SkatListActivity.INTENT_FLAG_SPIEL_FORTSETZEN);
                intent.putExtra("datum", datum);
                if (!datum.equals("")) {
                    startActivity(intent);
                }
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
        getMenuInflater().inflate(R.menu.menu_load_game, menu);
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
