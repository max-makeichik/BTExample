package com.mm.btexample.data.repository.repository_impl

import android.content.Context
import android.content.SharedPreferences
import com.mm.btexample.data.repository.repository_interface.IPrefsRepository
import javax.inject.Inject

class PrefsRepositoryImpl @Inject
constructor(context: Context) : IPrefsRepository {

    private val appPrefs: SharedPreferences

    init {
        appPrefs = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE)
    }

    override var btSwitchState: Boolean
        get() = appPrefs.getBoolean(BT_SWITCH_STATE, false)
        set(state) = appPrefs.edit().putBoolean(BT_SWITCH_STATE, state).apply()

    override fun hasBtSwitchState(): Boolean {
        return appPrefs.contains(BT_SWITCH_STATE)
    }

    companion object {

        private const val PREFS_PREFIX = "BTExample"
        private const val APP_PREFS_NAME = PREFS_PREFIX + "_APP"

        private const val BT_SWITCH_STATE = "bt.switch_state"
    }

}
