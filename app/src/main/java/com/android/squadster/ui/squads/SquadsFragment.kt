package com.android.squadster.ui.squads

import android.os.Bundle
import android.view.View
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.main.squads.SquadsPresenter
import com.android.squadster.main.squads.SquadsView
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class SquadsFragment : BaseFragment(), SquadsView {

    override val layoutRes = R.layout.fragment_squads

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var squadsPresenter: SquadsPresenter

    @ProvidePresenter
    fun providePresenter(): SquadsPresenter =
        scope.getInstance(SquadsPresenter::class.java)

    override fun onBackPressed() {
        squadsPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {}
}