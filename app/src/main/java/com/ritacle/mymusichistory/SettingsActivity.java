package com.ritacle.mymusichistory;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.ritacle.mymusichistory.utils.PlayersUtil;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String stringValue = value.toString();

                    if (preference instanceof ListPreference) {
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                    } else {
                        preference.setSummary(stringValue);
                    }

                    return true;
                }

            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            PlayersUtil playersUtil = new PlayersUtil(getContext());
            List<ApplicationInfo> musicPlayers = playersUtil.findPlayers();

            for (int i = 0; i < musicPlayers.size(); i++) {
                ApplicationInfo player = musicPlayers.get(i);
                PreferenceScreen preferenceScreen = getPreferenceManager().getPreferenceScreen();
                CheckBoxPreference checkBoxPreference = new CheckBoxPreference(getContext());
                checkBoxPreference.setTitle(playersUtil.getApplicationName(player));
                checkBoxPreference.setIcon(playersUtil.getApplicationIcon(player));
                checkBoxPreference.setKey("player." + player.packageName);
                checkBoxPreference.setDefaultValue(false);
                preferenceScreen.addItemFromInflater(checkBoxPreference);
            }

        }

    }
}