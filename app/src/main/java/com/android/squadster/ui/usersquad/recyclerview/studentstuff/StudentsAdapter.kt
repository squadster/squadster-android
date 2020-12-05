package com.android.squadster.ui.usersquad.recyclerview.studentstuff

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.model.data.server.model.Member
import com.android.squadster.ui.usersquad.recyclerview.OnClickSquadMember
import java.util.*
import kotlin.collections.ArrayList


class StudentsAdapter(
    private val handler: OnClickSquadMember,
    private val currentUserId: String,
    private val isCurrentUserCommander: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), DragAndDropCallback.ItemTouchHelperContract {

    private var members = ArrayList<Member>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_soldier, parent, false)

        return StudentViewHolder(itemView, handler, currentUserId, isCurrentUserCommander)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StudentViewHolder).bindItem(members[position])
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(members, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(members, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(viewHolder: StudentViewHolder) {
        viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.clearest_blue))
    }

    override fun onRowClear(viewHolder: StudentViewHolder) {
        viewHolder.itemView.setBackgroundColor(Color.WHITE)
        handler.membersPositionsChanged(members)
    }

    fun setData(listOfMembers: ArrayList<Member>?) {
        if (listOfMembers != null) {
            members = listOfMembers
            notifyDataSetChanged()
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

    fun addMember(member: Member) {
        members.add(member)
        notifyItemInserted(members.lastIndex)
    }

    fun findMember(id: String): Member? {
        return members.find {
            it.id == id
        }
    }

    fun isListOfMembersIsEmpty(): Boolean = members.isEmpty()
}