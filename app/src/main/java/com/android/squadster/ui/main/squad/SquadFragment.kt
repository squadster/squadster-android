package com.android.squadster.ui.main.squad

import android.os.Bundle
import android.view.View
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.main.squad.SquadPresenter
import com.android.squadster.main.squad.SquadView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class SquadFragment : BaseFragment(), SquadView {

    override val layoutRes = R.layout.fragment_squad

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var squadPresenter: SquadPresenter

    @ProvidePresenter
    fun providePresenter(): SquadPresenter =
        scope.getInstance(SquadPresenter::class.java)

    override fun onBackPressed() {
        squadPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {}
}