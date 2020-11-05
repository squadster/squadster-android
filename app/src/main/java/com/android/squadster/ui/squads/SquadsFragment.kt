package com.android.squadster.ui.squads

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.main.squads.SquadsPresenter
import com.android.squadster.main.squads.SquadsView
import com.android.squadster.model.data.server.apolloClient
import kotlinx.android.synthetic.main.fragment_squads.*
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

    private fun setupViews() {
        context?.let {
            squadsPresenter.loadUserAvatar(it, btn_profile)
        }

        btn_profile.setOnClickListener {

        }
    }
}