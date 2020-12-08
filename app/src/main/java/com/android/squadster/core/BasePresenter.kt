package com.android.squadster.core

import moxy.MvpPresenter
import moxy.MvpView

open class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    // TODO change all GlobalScope in Presenters to specific scopes for each screen

    override fun onDestroy() {}
}