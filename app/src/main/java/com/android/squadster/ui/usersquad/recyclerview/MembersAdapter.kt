package com.android.squadster.ui.usersquad.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.model.data.server.model.Member

class MembersAdapter(
    private val handler: OnClickSquadMember,
    private val currentUserId: String,
    private val isCurrentUserCommander: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var members = ArrayList<Member>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soldier, parent, false)

        return MemberViewHolder(itemView, handler, currentUserId, isCurrentUserCommander)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MemberViewHolder).bindItem(members[position])
    }

    override fun getItemCount(): Int {
        return members.size
    }

    fun setData(listOfMembers: ArrayList<Member>?) {
        if (listOfMembers != null) {
            members = listOfMembers
            notifyDataSetChanged()
        }
    }

    fun updateMember(id: String, role: String, queueNumber: Int) {
        val member = members.find {
            it.id == id
        }
        if (member != null) {
            val index = members.indexOf(member)
            member.role = role
            member.queueNumber = queueNumber
            notifyItemChanged(index)
        }
    }

    fun deleteMember(id: String) {
        val member = members.find {
            it.id == id
        }
        if (member != null) {
            val index = members.indexOf(member)
            members.remove(member)
            notifyItemRemoved(index)
        }
    }
}