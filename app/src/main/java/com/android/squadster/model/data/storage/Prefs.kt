package com.android.squadster.model.data.storage

import android.content.Context
import javax.inject.Inject

class Prefs @Inject constructor(
    private val context: Context
) {

    private fun getSharedPreferences(prefsName: String) =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    //region app
    private val APP_DATA = "app_data"
    private val KEY_FIRST_LAUNCH_TIME = "launch_ts"
    private val KEY_TOKEN = "token"
    private val appPrefs by lazy { getSharedPreferences(APP_DATA) }

    var firstLaunchTimeStamp: Long?
        get() = appPrefs.getLong(KEY_FIRST_LAUNCH_TIME, 0).takeIf { it > 0 }
        set(value) {
            appPrefs.edit().putLong(KEY_FIRST_LAUNCH_TIME, value ?: 0).apply()
        }

    var accessToken: String?
        get() = appPrefs.getString(KEY_TOKEN, "")
        set(value) {
            appPrefs.edit().putString(KEY_TOKEN, value ?: "").apply()
        }
}