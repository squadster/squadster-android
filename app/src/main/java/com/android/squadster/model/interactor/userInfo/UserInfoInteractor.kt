package com.android.squadster.model.interactor.userInfo

import com.android.squadster.model.data.server.ServerApi
import com.android.squadster.model.data.server.draftUserInfo.DraftUserInfo
import com.android.squadster.model.data.storage.Prefs
import com.android.squadster.model.system.scheduler.SchedulersProvider
import io.reactivex.Completable
import javax.inject.Inject

class UserInfoInteractor @Inject constructor(
    private val serverApi: ServerApi,
    private val schedulers: SchedulersProvider,
    private val prefs: Prefs,
    private val draftUserInfo: DraftUserInfo
) {

    fun getFullVKProfile(): Completable =
        serverApi
            .getFullVKProfile(
                draftUserInfo.userId,
                "5.52",
                draftUserInfo.token
            )
            .doOnSuccess { profile ->
                draftUserInfo.name = profile.response[0].firstName
                draftUserInfo.surname = profile.response[0].lastName
            }
            .ignoreElement()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}