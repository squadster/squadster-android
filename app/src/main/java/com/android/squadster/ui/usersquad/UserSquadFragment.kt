package com.android.squadster.ui.usersquad

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.model.data.server.model.RequestStatus
import com.android.squadster.screenslogic.squads.SquadsPresenter
import com.android.squadster.screenslogic.squads.SquadsView
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter
import com.android.squadster.screenslogic.usersquad.UserSquadView
import com.android.squadster.ui.squads.dialog.CreateSquadDialog
import com.android.squadster.ui.squads.recyclerview.OnClickItem
import com.android.squadster.ui.squads.recyclerview.SquadsAdapter
import com.squadster.server.GetSquadsQuery
import kotlinx.android.synthetic.main.fragment_squads.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class UserSquadFragment : BaseFragment(), UserSquadView {

    override val layoutRes = R.layout.fragment_user_squad

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var userSquadPresenter: UserSquadPresenter

    @ProvidePresenter
    fun providePresenter(): UserSquadPresenter =
        scope.getInstance(UserSquadPresenter::class.java)

    override fun onBackPressed() {
        userSquadPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    /*override fun setSquads(squads: List<GetSquadsQuery.Squad>) {
        srl_update_squads_list.isRefreshing = false
        activity?.runOnUiThread {
            if (squads.isEmpty()) {
                showEmptyListOfSquads()
            } else {
                cl_addition_info.visibility = View.VISIBLE
                rv_squads.visibility = View.VISIBLE
                squadsAdapter.setData(ArrayList(squads))
            }
        }
    }

    override fun updateSquadInvitation(
        squadId: String,
        requestId: String,
        requestStatus: RequestStatus
    ) {
        activity?.runOnUiThread {
            squadsAdapter.updateSquad(squadId, requestId, requestStatus)
        }
    }

    override fun showErrorMessage(message: String) {
        srl_update_squads_list.isRefreshing = false
        activity?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun showEmptyListOfSquads() {
        activity?.runOnUiThread {
            cl_addition_info.visibility = View.GONE
            rv_squads.visibility = View.GONE
            tv_no_squads_message.visibility = View.VISIBLE
        }
    }

    override fun sendRequest(squadId: String) {
        squadsPresenter.sendRequest(squadId)
    }

    override fun cancelRequest(requestId: String?) {
        squadsPresenter.cancelRequest(requestId)
    }*/

    private fun setupViews() {
        /*squadsPresenter.loadUserAvatar(context, iv_profile)

        iv_profile.setOnClickListener {
            squadsPresenter.goToProfile()
        }

        srl_update_squads_list.setOnRefreshListener {
            squadsPresenter.getSquads()
        }

        fl_create_squad.setOnClickListener {
            val createSquadDialog = CreateSquadDialog(requireContext(), ::createSquad)
            createSquadDialog.show()
        }

        squadsAdapter = SquadsAdapter(this, squadsPresenter.getCurrentUserId())
        val llManager = LinearLayoutManager(context)
        rv_squads.layoutManager = llManager
        rv_squads.adapter = squadsAdapter*/
    }

    /*private fun createSquad(squadNumber: String, classDay:Int){
        squadsPresenter.createSquad(squadNumber, classDay)
    }*/
}