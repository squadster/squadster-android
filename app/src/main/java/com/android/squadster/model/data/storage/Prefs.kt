package com.android.squadster.model.data.storage

import android.content.Context
import javax.inject.Inject

class Prefs @Inject constructor(
    private val context: Context
) {

    private fun getSharedPreferences() =
        context.getSharedPreferences(APP_DATA, Context.MODE_PRIVATE)

    private val APP_DATA = "app_data"
    private val KEY_TOKEN = "token"
    private val appPrefs by lazy { getSharedPreferences() }

    var accessToken: String?
        get() = appPrefs.getString(KEY_TOKEN, "")
        set(value) {
            appPrefs.edit().putString(KEY_TOKEN, value ?: "").apply()
        }
}