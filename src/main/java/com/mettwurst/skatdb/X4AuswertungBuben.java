package com.mettwurst.skatdb;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class X4AuswertungBuben extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.pref_4_auswertung_buben);

        final CheckBoxPreference[] preferences = new CheckBoxPreference[6];
        String[] keys = {"hand", "schneider", "schwarz", "ouvert", "kontra", "re"};
        for (int i = 0; i < keys.length; i++) {
            preferences[i] = (CheckBoxPreference) findPreference(keys[i]);
        }
/*
        final String spiel = "Kreuz"; // TODO DUMMY
        Preference.OnPreferenceClickListener listener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final boolean isChecked = ((CheckBoxPreference) preference).isChecked();

                switch (String.valueOf(preference.getTitle())) {
                    case "Hand":
                        if (isChecked) {
                        } else {
                            preferences[1].setChecked(false);
                            preferences[2].setChecked(false);
                            preferences[3].setChecked(false);
                        }
                        break;
                    case "Schneider":
                        if (isChecked) {
                            preferences[0].setChecked(true);
                        } else {
                            preferences[2].setChecked(false);
                            preferences[3].setChecked(false);
                        }
                        break;
                    case "Schwarz":
                        if (isChecked) {
                            preferences[0].setChecked(true);
                            preferences[1].setChecked(true);
                        } else {
                            preferences[3].setChecked(false);
                        }
                        break;
                    case "Ouvert":
                        if (isChecked) {
                            preferences[0].setChecked(true);
                            preferences[1].setChecked(true);
                            preferences[2].setChecked(true);
                        }
                        break;
                    case "Kontra":
                        if (!isChecked) {
                            preferences[5].setChecked(false);
                        }
                        break;
                    case "Re":
                        if (isChecked) {
                            preferences[4].setChecked(true);
                        }
                        break;
                }
                return false;
            }
        };

        Preference preference;
        for (int i = 0; i < keys.length; i++) {
            preference = findPreference(keys[i]);
            preference.setOnPreferenceClickListener(listener);
        }*/
    }
}
