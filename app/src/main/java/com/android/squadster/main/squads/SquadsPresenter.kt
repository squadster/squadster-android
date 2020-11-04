package com.android.squadster.main.squads

import com.android.squadster.core.BasePresenter
import com.android.squadster.core.ErrorHandler
import com.android.squadster.core.FlowRouter
import com.android.squadster.model.system.resource.ResourceManager
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SquadsPresenter @Inject constructor(
    private val flowRouter: FlowRouter,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : BasePresenter<SquadsView>() {

    fun onBackPressed() {
        flowRouter.finishFlow()
    }
}