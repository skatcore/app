package com.mettwurst.skatdb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.nio.channels.SelectionKey;

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
public class X3Ansagen extends PreferenceActivity {

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
        boolean isNullspiel = SkatInfoSingleton.getInstance().spiel.equals("Null");
        if (isNullspiel) {
            addPreferencesFromResource(R.xml.pref_3_ansagen_null);
        } else {
            addPreferencesFromResource(R.xml.pref_3_ansagen_normal);
        }

        Intent intent = getIntent();
        if (intent.getBooleanExtra("vor", false)) {
            // Alte Eingaben leeren
            ((CheckBoxPreference) findPreference("hand")).setChecked(false);
            ((CheckBoxPreference) findPreference("ouvert")).setChecked(false);
            ((CheckBoxPreference) findPreference("kontra")).setChecked(false);
            ((CheckBoxPreference) findPreference("re")).setChecked(false);
            if (!isNullspiel) {
                ((CheckBoxPreference) findPreference("schneider")).setChecked(false);
                ((CheckBoxPreference) findPreference("schwarz")).setChecked(false);
            }
        }

        final CheckBoxPreference[] preferences;
        String[] keys;

        if (!SkatInfoSingleton.getInstance().spiel.equals("Null")) {
            preferences = new CheckBoxPreference[6];
            keys = new String[]{"hand", "schneider", "schwarz", "ouvert", "kontra", "re"};
        } else {
            preferences = new CheckBoxPreference[4];
            keys = new String[]{"hand", "ouvert", "kontra", "re"};
        }
        for (int i = 0; i < keys.length; i++) {
            preferences[i] = (CheckBoxPreference) findPreference(keys[i]);
        }

        Preference.OnPreferenceClickListener checkBoxListenerNormal = new Preference.OnPreferenceClickListener() {
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

        Preference.OnPreferenceClickListener checkBoxListenerNull = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final boolean isChecked = ((CheckBoxPreference) preference).isChecked();

                switch (String.valueOf(preference.getTitle())) {
                    case "Kontra":
                        if (!isChecked) {
                            preferences[3].setChecked(false);
                        }
                        break;
                    case "Re":
                        if (isChecked) {
                            preferences[2].setChecked(true);
                        }
                        break;
                }
                return false;
            }
        };

        // Assign listener
        Preference preference;
        for (int i = 0; i < keys.length; i++) {
            preference = findPreference(keys[i]);
            if (isNullspiel) {
                preference.setOnPreferenceClickListener(checkBoxListenerNull);
            } else {
                preference.setOnPreferenceClickListener(checkBoxListenerNormal);
            }
        }

        preference = findPreference("weiter");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SkatInfoSingleton infoSingleton = SkatInfoSingleton.getInstance();
                boolean isNullspiel = infoSingleton.spiel.equals("Null");

                infoSingleton.handspiel = ((CheckBoxPreference) findPreference("hand")).isChecked() ? 1 : 0;
                infoSingleton.ouvert = ((CheckBoxPreference) findPreference("ouvert")).isChecked() ? 1 : 0;
                if (isNullspiel) {
                    infoSingleton.schneider_angesagt = 0;
                    infoSingleton.schwarz_angesagt = 0;
                } else {
                    infoSingleton.schneider_angesagt = ((CheckBoxPreference) findPreference("schneider")).isChecked() ? 1 : 0;
                    infoSingleton.schwarz_angesagt = ((CheckBoxPreference) findPreference("schwarz")).isChecked() ? 1 : 0;
                }
                infoSingleton.kontra = ((CheckBoxPreference) findPreference("kontra")).isChecked() ? 1 : 0;
                infoSingleton.re = ((CheckBoxPreference) findPreference("re")).isChecked() ? 1 : 0;

                Intent intent = new Intent(getApplicationContext(), X4AuswertungBuben.class);
                startActivity(intent);
                return false;
            }
        });
    }
}