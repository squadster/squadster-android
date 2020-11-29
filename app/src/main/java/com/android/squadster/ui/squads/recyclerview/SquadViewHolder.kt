package com.android.squadster.ui.squads.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.model.data.server.model.Squad
import com.android.squadster.ui.usersquad.recyclerview.MemberViewHolder
import kotlinx.android.synthetic.main.item_squad.view.*

class SquadViewHolder(
    view: View,
    private val handler: OnClickSquad,
    private val currentUserId: String,
    private val isUserAvailableInteractWithSquads: Boolean
) : RecyclerView.ViewHolder(view) {

    private lateinit var squad: Squad
    private var currentUserRequestId: String? = null

    fun bindItem(squad: Squad) {
        this.squad = squad

        setupViews()
    }

    private fun setupViews() {
        itemView.tv_squad_number.text = squad.squadNumber
        val commander = squad.members.find { member ->
            member.role == MemberViewHolder.COMMANDER
        }?.user
        val name = commander?.let { user ->
            "${user.firstName} ${user.lastName}"
        } ?: ""

        itemView.tv_squad_commander.text = name

        if (isUserAvailableInteractWithSquads) {
            val currentUserInSquad = squad.members.find {
                it.user?.id == currentUserId
            }
            if (currentUserInSquad != null) {
                itemView.iv_send_request.visibility = View.GONE
                itemView.iv_cancel_request.visibility = View.GONE
            } else {
                val requestByCurrentUser = squad.requests.find {
                    it.user?.id == currentUserId
                }

                if (requestByCurrentUser != null) {
                    itemView.iv_send_request.visibility = View.GONE
                    itemView.iv_cancel_request.visibility = View.VISIBLE

                    currentUserRequestId = requestByCurrentUser.id

                    itemView.iv_cancel_request.setOnClickListener {
                        handler.cancelRequest(currentUserRequestId)
                    }
                } else {
                    itemView.iv_cancel_request.visibility = View.GONE
                    itemView.iv_send_request.visibility = View.VISIBLE
                    itemView.iv_send_request.setOnClickListener {
                        handler.sendRequest(squad.id)
                    }
                }
            }
        } else {
            itemView.iv_send_request.visibility = View.GONE
            itemView.iv_cancel_request.visibility = View.GONE
        }
    }
}
