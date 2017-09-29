package khaliliyoussef.newsapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import khaliliyoussef.newsapp.R;


public class SettingsActivity extends AppCompatActivity {

    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.main_settings);

            Preference newsNumber = findPreference
                    (getString(R.string.news_number_kye));
            bindPreferenceToValue(newsNumber);

            Preference orderBy = findPreference(getString(R.string.order_by_key));
            bindPreferenceToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object mValue) {
            String value = mValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
            preference.setSummary(value);
            return true;
        }

        private void bindPreferenceToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                    (preference.getContext());
            String preferenceString = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}