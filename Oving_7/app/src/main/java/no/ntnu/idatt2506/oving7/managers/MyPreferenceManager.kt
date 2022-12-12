package no.ntnu.idatt2506.oving7.managers

import android.content.SharedPreferences
import android.graphics.Color

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import no.ntnu.idatt2506.oving7.R

class MyPreferenceManager(private val activity: AppCompatActivity) {
    private val resources = activity.resources
    private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    fun changeBackgroundColor() {
        val backgroundValues = resources.getStringArray(R.array.background_values)
        val value = getString(
            resources.getString(R.string.background_mode),
            resources.getString(R.string.background_default_value)
        )
        when (value) {
            backgroundValues[0] -> putString("BackgroundColor", backgroundValues[0])
            backgroundValues[1] -> putString("BackgroundColor", backgroundValues[1])
            backgroundValues[2] -> putString("BackgroundColor", backgroundValues[2])
            backgroundValues[3] -> putString("BackgroundColor", backgroundValues[3])
            backgroundValues[4] -> putString("BackgroundColor", backgroundValues[4])
        }
        activity.window.decorView.rootView.setBackgroundColor(Color.parseColor(getString("BackgroundColor", "#FFFFFF")))
    }


    fun registerListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(activity)
    }

    fun unregisterListener(activity: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.unregisterOnSharedPreferenceChangeListener(activity)
    }
}