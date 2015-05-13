package com.mettwurst.skatdb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class InspectSkatrundeActivity extends ActivityWithSkatinfo {
    public long _id;

    private ArrayList<ListItem> arrayList;
    private DBController dbCon;
    private ArrayAdapter adapter;

    public class ListItem {
        private String title;
        private Object value;
        private String stringValue;

        public ListItem(String title, Object value) {
            this.title = title;
            setValue(value);
        }

        public String toString() {
            return title +": " +value;
        }

        public void setValue(Object value) {
            this.value = value;
            this.stringValue = String.valueOf(value);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_skatrunde);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        _id = Long.valueOf(id);
        dbCon = new DBController(this);
        dbCon.open();
        Cursor cursor = dbCon.fetch(id);
        fillFieldsFromCursor(cursor);
        fillArrayList();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        ListView listView = (ListView) findViewById(R.id.lvInspectSkatrunde);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ListItem clickedItem = arrayList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(InspectSkatrundeActivity.this);
                builder.setTitle(clickedItem.title);
                final EditText editText = new EditText(InspectSkatrundeActivity.this);
                editText.setText(clickedItem.stringValue);

                final boolean string_type;
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 9:
                    case 15:
                    case 23: string_type = true; break;
                    default: string_type = false;
                }
                if (string_type) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                } else {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                builder.setView(editText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newValue = editText.getText().toString();
                        clickedItem.setValue(newValue);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void fillArrayList() {
        arrayList = new ArrayList<>();

        arrayList.add(new ListItem("Datum", datum));
        arrayList.add(new ListItem("Spieler1", spieler1));
        arrayList.add(new ListItem("Spieler2", spieler2));
        arrayList.add(new ListItem("Spieler3", spieler3));
        arrayList.add(new ListItem("Spieler4", spieler4));
        arrayList.add(new ListItem("Spieler5", spieler5));
        arrayList.add(new ListItem("Spielerzahl", spielerzahl));
        arrayList.add(new ListItem("Geber", geber));
        arrayList.add(new ListItem("Bock", bock));

        arrayList.add(new ListItem("Solist", solist));
        arrayList.add(new ListItem("Handspiel", handspiel));
        arrayList.add(new ListItem("Ouvert", ouvert));
        arrayList.add(new ListItem("Schneider angesagt", schneider_angesagt));
        arrayList.add(new ListItem("Schwarz angesagt", schwarz_angesagt));
        arrayList.add(new ListItem("Reizwert", reizwert));
        arrayList.add(new ListItem("Spiel", spiel));
        arrayList.add(new ListItem("Kontra", kontra));
        arrayList.add(new ListItem("Re", re));
        arrayList.add(new ListItem("Jungfrau angesagt 1", jungfrau_angesagt1));
        arrayList.add(new ListItem("Jungfrau angesagt 2", jungfrau_angesagt2));
        arrayList.add(new ListItem("Jungfrau angesagt 3", jungfrau_angesagt3));
        arrayList.add(new ListItem("Jungfrau angesagt 4", jungfrau_angesagt4));
        arrayList.add(new ListItem("Jungfrau angesagt 5", jungfrau_angesagt5));

        arrayList.add(new ListItem("Buben", buben));
        arrayList.add(new ListItem("Schneider gespiel", schneider_gespielt));
        arrayList.add(new ListItem("Schwarz gespiel", schwarz_gespielt));
        arrayList.add(new ListItem("Punkte Re", punkte_re));
        arrayList.add(new ListItem("Solist gewonnen", solist_gewonnen));
        arrayList.add(new ListItem("Punkte Spieler 1", punkte_sp1));
        arrayList.add(new ListItem("Punkte Spieler 2", punkte_sp2));
        arrayList.add(new ListItem("Punkte Spieler 3", punkte_sp3));
        arrayList.add(new ListItem("Punkte Spieler 4", punkte_sp4));
        arrayList.add(new ListItem("Punkte Spieler 5", punkte_sp5));
        arrayList.add(new ListItem("Gesamt Spieler 1", gesamt_sp1));
        arrayList.add(new ListItem("Gesamt Spieler 2", gesamt_sp2));
        arrayList.add(new ListItem("Gesamt Spieler 3", gesamt_sp3));
        arrayList.add(new ListItem("Gesamt Spieler 4", gesamt_sp4));
        arrayList.add(new ListItem("Gesamt Spieler 5", gesamt_sp5));
        arrayList.add(new ListItem("Spielwert", spielwert));
    }

    private void fillFieldsFromCursor(Cursor c) {
        datum = c.getString(c.getColumnIndex(DBContract.Entry.COL_DATUM));
        spieler1 = c.getString(c.getColumnIndex(DBContract.Entry.COL_SPIELER1));
        spieler2 = c.getString(c.getColumnIndex(DBContract.Entry.COL_SPIELER2));
        spieler3 = c.getString(c.getColumnIndex(DBContract.Entry.COL_SPIELER3));
        spieler4 = c.getString(c.getColumnIndex(DBContract.Entry.COL_SPIELER4));
        spieler5 = c.getString(c.getColumnIndex(DBContract.Entry.COL_SPIELER5));
        spielerzahl = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SPIELERZAHL));
        geber = c.getInt(c.getColumnIndex(DBContract.Entry.COL_GEBER));
        bock = c.getInt(c.getColumnIndex(DBContract.Entry.COL_BOCK));

        solist = c.getString(c.getColumnIndex(DBContract.Entry.COL_SOLIST));
        handspiel = c.getInt(c.getColumnIndex(DBContract.Entry.COL_HANDSPIEL));
        ouvert = c.getInt(c.getColumnIndex(DBContract.Entry.COL_OUVERT));
        schneider_angesagt = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SCHNEIDER_ANGESAGT));
        schwarz_angesagt = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SCHWARZ_ANGESAGT));
        reizwert = c.getInt(c.getColumnIndex(DBContract.Entry.COL_REIZWERT));
        spiel = c.getString(c.getColumnIndex(DBContract.Entry.COL_SPIEL));
        kontra = c.getInt(c.getColumnIndex(DBContract.Entry.COL_KONTRA));
        re = c.getInt(c.getColumnIndex(DBContract.Entry.COL_RE));
        jungfrau_angesagt1 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT1));
        jungfrau_angesagt2 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT2));
        jungfrau_angesagt3 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT3));
        jungfrau_angesagt4 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT4));
        jungfrau_angesagt5 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_JUNGFRAU_ANGESAGT5));

        buben = c.getString(c.getColumnIndex(DBContract.Entry.COL_BUBEN));
        schneider_gespielt = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SCHNEIDER_GESPIELT));
        schwarz_gespielt = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SCHWARZ_GESPIELT));
        punkte_re = c.getInt(c.getColumnIndex(DBContract.Entry.COL_PUNKTE_RE));
        solist_gewonnen = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SOLIST_GEWONNEN));
        punkte_sp1 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP1));
        punkte_sp2 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP2));
        punkte_sp3 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP3));
        punkte_sp4 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP4));
        punkte_sp5 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_PUNKTE_SP5));
        gesamt_sp1 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_GESAMT_SP1));
        gesamt_sp2 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_GESAMT_SP2));
        gesamt_sp3 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_GESAMT_SP3));
        gesamt_sp4 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_GESAMT_SP4));
        gesamt_sp5 = c.getInt(c.getColumnIndex(DBContract.Entry.COL_GESAMT_SP5));
        spielwert = c.getInt(c.getColumnIndex(DBContract.Entry.COL_SPIELWERT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inspect_skatrunde, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menuSaveUpdate) {
            updateFieldsFromArrayList();
            dbCon.update(_id, this);
            Intent main = new Intent(InspectSkatrundeActivity.this, SkatListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFieldsFromArrayList() {
        datum               = arrayList.get(0).stringValue;
        spieler1            = arrayList.get(1).stringValue;
        spieler2            = arrayList.get(2).stringValue;
        spieler3            = arrayList.get(3).stringValue;
        spieler4            = arrayList.get(4).stringValue;
        spieler5            = arrayList.get(5).stringValue;
        spielerzahl         = Integer.valueOf(arrayList.get(6).stringValue);
        geber               = Integer.valueOf(arrayList.get(7).stringValue);
        bock                = Integer.valueOf(arrayList.get(8).stringValue);
        solist              = arrayList.get(9).stringValue;
        handspiel           = Integer.valueOf(arrayList.get(10).stringValue);
        ouvert              = Integer.valueOf(arrayList.get(11).stringValue);
        schneider_angesagt  = Integer.valueOf(arrayList.get(12).stringValue);
        schwarz_angesagt    = Integer.valueOf(arrayList.get(13).stringValue);
        reizwert            = Integer.valueOf(arrayList.get(14).stringValue);
        spiel               = arrayList.get(15).stringValue;
        kontra              = Integer.valueOf(arrayList.get(16).stringValue);
        re                  = Integer.valueOf(arrayList.get(17).stringValue);
        jungfrau_angesagt1  = Integer.valueOf(arrayList.get(18).stringValue);
        jungfrau_angesagt2  = Integer.valueOf(arrayList.get(19).stringValue);
        jungfrau_angesagt3  = Integer.valueOf(arrayList.get(20).stringValue);
        jungfrau_angesagt4  = Integer.valueOf(arrayList.get(21).stringValue);
        jungfrau_angesagt5  = Integer.valueOf(arrayList.get(22).stringValue);
        buben               = arrayList.get(23).stringValue;
        schneider_gespielt  = Integer.valueOf(arrayList.get(24).stringValue);
        schwarz_gespielt    = Integer.valueOf(arrayList.get(25).stringValue);
        punkte_re           = Integer.valueOf(arrayList.get(26).stringValue);
        solist_gewonnen     = Integer.valueOf(arrayList.get(27).stringValue);
        punkte_sp1          = Integer.valueOf(arrayList.get(28).stringValue);
        punkte_sp2          = Integer.valueOf(arrayList.get(29).stringValue);
        punkte_sp3          = Integer.valueOf(arrayList.get(30).stringValue);
        punkte_sp4          = Integer.valueOf(arrayList.get(31).stringValue);
        punkte_sp5          = Integer.valueOf(arrayList.get(32).stringValue);
        gesamt_sp1          = Integer.valueOf(arrayList.get(33).stringValue);
        gesamt_sp2          = Integer.valueOf(arrayList.get(34).stringValue);
        gesamt_sp3          = Integer.valueOf(arrayList.get(35).stringValue);
        gesamt_sp4          = Integer.valueOf(arrayList.get(36).stringValue);
        gesamt_sp5          = Integer.valueOf(arrayList.get(37).stringValue);
        spielwert           = Integer.valueOf(arrayList.get(38).stringValue);
    }
}
