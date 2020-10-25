package com.android.squadster.model.interactor.launch

import com.android.squadster.entity.LaunchInfo
import com.android.squadster.model.data.storage.Prefs
import javax.inject.Inject

class LaunchInteractor @Inject constructor(
    private val prefs: Prefs
) {

    fun getLaunchInfo(): LaunchInfo {
        val timeStamp = prefs.firstLaunchTimeStamp
        val launchInfo = LaunchInfo(timeStamp == null)

        if (timeStamp == null) {
            prefs.firstLaunchTimeStamp = System.currentTimeMillis()
        }
        return launchInfo
    }
}