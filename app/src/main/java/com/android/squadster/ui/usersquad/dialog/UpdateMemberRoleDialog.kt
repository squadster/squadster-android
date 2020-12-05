package com.android.squadster.ui.usersquad.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.android.squadster.R
import com.android.squadster.screenslogic.usersquad.UserSquadPresenter
import kotlinx.android.synthetic.main.dialog_update_member_role.*

class UpdateMemberRoleDialog(
    context: Context,
    private val id: String,
    private val oldRole: String,
    private val queuenumber: Int,
    private val handler: (String, String, String, Int) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_update_member_role)

        setupViews()
    }

    private fun setupViews() {
        btn_cross.setOnClickListener {
            dismiss()
        }

        when (oldRole) {
            UserSquadPresenter.COMMANDER -> btn_commander.isEnabled = false
            UserSquadPresenter.DEPUTY_COMMANDER -> btn_deputy_commander.isEnabled = false
            UserSquadPresenter.JOURNALIST -> btn_journalist.isEnabled = false
            else -> btn_student.isEnabled = false
        }

        btn_commander.setOnClickListener {
            handler(id, oldRole, UserSquadPresenter.COMMANDER, queuenumber)
            dismiss()
        }
        btn_deputy_commander.setOnClickListener {
            handler(id, oldRole, UserSquadPresenter.DEPUTY_COMMANDER, queuenumber)
            dismiss()
        }
        btn_journalist.setOnClickListener {
            handler(id, oldRole, UserSquadPresenter.JOURNALIST, queuenumber)
            dismiss()
        }
        btn_student.setOnClickListener {
            handler(id, oldRole, UserSquadPresenter.STUDENT, queuenumber)
            dismiss()
        }
    }


    override fun onStart() {
        super.onStart()

        window?.let { w ->
            w.setBackgroundDrawable(null)
            w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}