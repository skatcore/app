package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NeuesSpielActivity extends Activity {
    private String datum;
    private String spieler1 = "";
    private String spieler2 = "";
    private String spieler3 = "";
    private String spieler4 = "";
    private String spieler5 = "";
    private int spielerzahl = -1;

    private TextView tvSpielerzahl;
    private EditText edSpielername1;
    private EditText edSpielername2;
    private EditText edSpielername3;
    private EditText edSpielername4;
    private EditText edSpielername5;

    private TextWatcher twUpdateEditTextsVisibility = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            spieler1 = edSpielername1.getText().toString();
            spieler2 = edSpielername2.getText().toString();
            spieler3 = edSpielername3.getText().toString();
            spieler4 = edSpielername4.getText().toString();
            spieler5 = edSpielername5.getText().toString();

            if ((spieler1.equals("") || spieler2.equals("") || spieler3.equals(""))) {
                spielerzahl = -1;
                edSpielername4.setVisibility(View.INVISIBLE);
                edSpielername5.setVisibility(View.INVISIBLE);
            } else {
                edSpielername4.setVisibility(View.VISIBLE);
                if (spieler4.equals("")) {
                    spielerzahl = 3;
                    edSpielername5.setVisibility(View.INVISIBLE);
                } else {
                    spielerzahl = 4;
                    edSpielername5.setVisibility(View.VISIBLE);
                    if (!spieler5.equals("")) {
                        spielerzahl = 5;
                    }
                }
            }
            if (spielerzahl >= 3) {
                tvSpielerzahl.setText(spielerzahl + "Spieler");
            } else tvSpielerzahl.setText("");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neues_spiel);

        setTitle("Neues Spiel");
        TextView tvDatumUhrzeit  = (TextView) findViewById(R.id.tvDatumUhrzeit);
        tvSpielerzahl   = (TextView) findViewById(R.id.tvSpielerzahl);
        edSpielername1  = (EditText) findViewById(R.id.edSpielername1);
        edSpielername2  = (EditText) findViewById(R.id.edSpielername2);
        edSpielername3  = (EditText) findViewById(R.id.edSpielername3);
        edSpielername4  = (EditText) findViewById(R.id.edSpielername4);
        edSpielername5  = (EditText) findViewById(R.id.edSpielername5);
        datum = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.GERMANY).format(Calendar.getInstance().getTime());
        tvDatumUhrzeit.setText(datum);
        edSpielername1.addTextChangedListener(twUpdateEditTextsVisibility);
        edSpielername2.addTextChangedListener(twUpdateEditTextsVisibility);
        edSpielername3.addTextChangedListener(twUpdateEditTextsVisibility);
        edSpielername4.addTextChangedListener(twUpdateEditTextsVisibility);
        edSpielername5.addTextChangedListener(twUpdateEditTextsVisibility);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_neues_spiel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menuSpielStarten) {
            if (spielerzahl < 3) {
                Toast.makeText(getApplicationContext(), "Spieler eintragen.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), SkatListActivity.class);
                intent.putExtra("neuesSpiel", true);
                intent.putExtra("datum", datum);
                intent.putExtra("spieler1", spieler1);
                intent.putExtra("spieler2", spieler2);
                intent.putExtra("spieler3", spieler3);
                intent.putExtra("spieler4", (spielerzahl >= 4) ? spieler4 : "");
                intent.putExtra("spieler5", (spielerzahl >= 5) ? spieler5 : "");
                intent.putExtra("spielerzahl", spielerzahl);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
