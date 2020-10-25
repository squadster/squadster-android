package com.android.squadster.main.squad

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.model.system.resource.ResourceManager
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SquadPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<SquadView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }
}