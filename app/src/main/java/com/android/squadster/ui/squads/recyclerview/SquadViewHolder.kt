package com.android.squadster.ui.squads.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.model.data.server.model.RequestStatus
import com.android.squadster.model.data.server.model.Squad
import kotlinx.android.synthetic.main.item_squad.view.*

class SquadViewHolder(
    view: View,
    private val handler: OnClickItem,
    private val currentUserId: String
) : RecyclerView.ViewHolder(view) {

    private lateinit var squad: Squad
    private var currentPosition: Int = 0
    private var currentUserRequestId: String? = null

    fun bindItem(squad: Squad, position: Int) {
        this.squad = squad
        currentPosition = position

        setupViews()
    }

    private fun setupViews() {
        itemView.tv_squad_number.text = squad.squadNumber
        val commander = squad.members.find { member ->
            member.role == COMMANDER
        }?.user
        val name = commander?.let { user ->
            "${user.firstName} ${user.lastName}"
        } ?: ""

        itemView.tv_squad_commander.text = name

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
    }

    companion object {
        private const val COMMANDER = "commander"
    }
}
