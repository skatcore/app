package com.mettwurst.skatdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifySkatrundeActivity extends Activity implements View.OnClickListener {
    private EditText editSolist;
    private EditText editGametype;
    private EditText editJacks;
    private Button btnUpdate;
    private Button btnDelete;

    private DBController dbCon;
    public long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Skatspiel");
        setContentView(R.layout.activity_modify_skatspiel);

        dbCon = new DBController(this);
        dbCon.open();

        // TODO
        editSolist = (EditText) findViewById(R.id.editSolist);
        editGametype = (EditText) findViewById(R.id.editGametype);
        editJacks = (EditText) findViewById(R.id.editJacks);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        // TODO
        String id = intent.getStringExtra("id");


        String solist = intent.getStringExtra("solist");
        String gametype = intent.getStringExtra("gametype");
        String jacks = ""; // TODO: Send via intent
        _id = Long.parseLong(id);

        // TODO
        editSolist.setText(solist);
        editGametype.setText(gametype);
        editJacks.setText(jacks);

        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify_skatspiel, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                // TODO
                /*
                String solist = editSolist.getText().toString();
                String gametype = editGametype.getText().toString();
                String jacks = editJacks.getText().toString();

                // TODO
                dbCon.update(_id, solist, gametype, jacks);
                */
                Toast.makeText(getApplicationContext(), "TODO", Toast.LENGTH_SHORT).show();
                this.returnHome();
                break;
            case R.id.btnDelete:
                // TODO: Update following rows
                dbCon.delete(_id);
                this.returnHome();
                break;
            default:
                break;
        }
    }

    private void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), SkatListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
