package com.android.squadster.screenslogic.profile

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

interface ProfileView : MvpView {

    @AddToEnd
    fun showUserInfo(nameAndSurname: String, birthday: String, faculty: String)
}