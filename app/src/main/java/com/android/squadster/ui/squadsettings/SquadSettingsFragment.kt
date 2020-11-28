package com.android.squadster.ui.squadsettings

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.squadster.R
import com.android.squadster.core.BaseFragment
import com.android.squadster.screenslogic.squadsettings.SquadSettingsPresenter
import com.android.squadster.screenslogic.squadsettings.SquadSettingsView
import com.android.squadster.ui.squadsettings.recyclerview.OnClickRequest
import com.android.squadster.ui.squadsettings.recyclerview.RequestsAdapter
import kotlinx.android.synthetic.main.fragment_squad_settings.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Scope

class SquadSettingsFragment : BaseFragment(), SquadSettingsView, OnClickRequest {

    override val layoutRes = R.layout.fragment_squad_settings

    private lateinit var requestsAdapter: RequestsAdapter

    override fun installScopeModules(scope: Scope) {
    }

    @InjectPresenter
    lateinit var squadSettingsPresenter: SquadSettingsPresenter

    @ProvidePresenter
    fun providePresenter(): SquadSettingsPresenter =
        scope.getInstance(SquadSettingsPresenter::class.java)

    override fun onBackPressed() {
        squadSettingsPresenter.onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }


    override fun showErrorMessage(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun acceptRequest(id: String) {
        squadSettingsPresenter.acceptRequest(id)
    }

    override fun rejectRequest(id: String) {
        squadSettingsPresenter.rejectRequest(id)
    }

    override fun requestsListIsEmpty() {
        rv_requests.visibility = View.GONE
        tv_empty_request_message.visibility = View.VISIBLE
    }

    override fun updateRequest(id: String) {
        activity?.runOnUiThread {
            requestsAdapter.deleteRequest(id)
        }
    }

    override fun deleteSquad() {
        activity?.runOnUiThread {
            squadSettingsPresenter.goToSquads()
        }
    }

    private fun setupViews() {
        val squadNumber =
            squadSettingsPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.squadNumber
        txt_squad_number.text = getString(R.string.user_squad, squadNumber)

        val classDayAndPosition = squadSettingsPresenter.getClassDay()
        txt_squad_class_day.text = getString(R.string.class_day_settings, classDayAndPosition.first)
        sp_squad_class_day.setSelection(classDayAndPosition.second)

        val announcement =
            squadSettingsPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.advertisment
                ?: ""
        txt_squad_announcement.text = getString(R.string.announcement, announcement)

        toolbar_squad_settings.setNavigationOnClickListener {
            squadSettingsPresenter.onBackPressed()
        }

        iv_edit_squad_number.setOnClickListener {
            ll_squad_number.visibility = View.GONE
            ll_new_squad_number.visibility = View.VISIBLE
        }

        iv_edit_squad_class_day.setOnClickListener {
            ll_squad_class_day.visibility = View.GONE
            ll_new_squad_class_day.visibility = View.VISIBLE
        }

        iv_edit_squad_announcement.setOnClickListener {
            et_squad_announcement.setText(announcement)
            ll_squad_announcement.visibility = View.GONE
            ll_new_squad_announcement.visibility = View.VISIBLE
        }

        iv_send_new_number.setOnClickListener {
            squadSettingsPresenter.updateSquadNumber(tie_squad_number.text.toString())
            txt_squad_number.text = getString(R.string.user_squad, tie_squad_number.text)
            ll_squad_number.visibility = View.VISIBLE
            ll_new_squad_number.visibility = View.GONE
            hideKeyboard(it)
        }

        iv_reject_new_number.setOnClickListener {
            ll_squad_number.visibility = View.VISIBLE
            ll_new_squad_number.visibility = View.GONE
            hideKeyboard(it)
        }

        iv_send_new_class_day.setOnClickListener {
            squadSettingsPresenter.updateSquadClassDay(sp_squad_class_day.selectedItemPosition + 1)
            txt_squad_class_day.text =
                getString(R.string.class_day_settings, sp_squad_class_day.selectedItem)
            ll_squad_class_day.visibility = View.VISIBLE
            ll_new_squad_class_day.visibility = View.GONE
            hideKeyboard(it)
        }

        iv_reject_new_class_day.setOnClickListener {
            ll_squad_class_day.visibility = View.VISIBLE
            ll_new_squad_class_day.visibility = View.GONE
            hideKeyboard(it)
        }

        iv_send_new_announcement.setOnClickListener {
            squadSettingsPresenter.updateSquadAnnouncement(et_squad_announcement.text.toString())
            txt_squad_announcement.text = getString(
                R.string.announcement,
                et_squad_announcement.text
            )
            ll_squad_announcement.visibility = View.VISIBLE
            ll_new_squad_announcement.visibility = View.GONE
            hideKeyboard(it)
        }

        iv_reject_new_announcement.setOnClickListener {
            ll_squad_announcement.visibility = View.VISIBLE
            ll_new_squad_announcement.visibility = View.GONE
            hideKeyboard(it)
        }

        btn_delete_squad.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.need_confirmation)
                .setMessage(R.string.delete_squad_message)
                .setCancelable(true)
                .setPositiveButton(R.string.yes) { _, _ -> squadSettingsPresenter.deleteSquad() }
                .setNegativeButton(R.string.no) { dialog, _ -> dialog.cancel() }
                .show()
        }

        when (squadSettingsPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.linkInvitationsEnabled
            ?: false) {
            true -> {
                btn_update_link_invitation.setText(R.string.turn_off_link_invitation)
            }
            false -> {
                btn_update_link_invitation.setText(R.string.turn_on_link_invitation)
            }
        }
        btn_update_link_invitation.setOnClickListener {
            val linkInvitationEnabled =
                squadSettingsPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.linkInvitationsEnabled
                    ?: false
            squadSettingsPresenter.updateLinkInvitation(!linkInvitationEnabled)
            when (!linkInvitationEnabled) {
                true -> {
                    btn_update_link_invitation.setText(R.string.turn_off_link_invitation)
                }
                false -> {
                    btn_update_link_invitation.setText(R.string.turn_on_link_invitation)
                }
            }
        }

        requestsAdapter = RequestsAdapter(this)
        val llManager = LinearLayoutManager(context)
        rv_requests.layoutManager = llManager
        rv_requests.adapter = requestsAdapter
        requestsAdapter.setData(squadSettingsPresenter.draftUserInfo.currentUserInfo?.squadMember?.squad?.requests)
    }

    private fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }
}