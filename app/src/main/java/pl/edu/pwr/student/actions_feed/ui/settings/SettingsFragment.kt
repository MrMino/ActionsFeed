package pl.edu.pwr.student.actions_feed.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pl.edu.pwr.student.actions_feed.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}