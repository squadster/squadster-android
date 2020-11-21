package com.android.squadster.ui.squads.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.extension.toSquad
import com.android.squadster.model.data.server.model.RequestStatus
import com.android.squadster.model.data.server.model.Squad
import com.squadster.server.GetSquadsQuery

class SquadsAdapter(
    private val handler: OnClickItem,
    private val currentUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var squads = ArrayList<Squad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_squad, parent, false)

        return SquadViewHolder(itemView, handler, currentUserId)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SquadViewHolder).bindItem(squads[position], position)
    }

    override fun getItemCount(): Int {
        return squads.size
    }

    fun setData(listOfSquads: ArrayList<GetSquadsQuery.Squad>) {
        squads = ArrayList(listOfSquads.map { it.toSquad() })
        notifyDataSetChanged()
    }

    fun updateSquad(squadId: String, requestId: String, requestStatus: RequestStatus) {
        val squad = squads.find {
            it.id == squadId
        }

        if (squad != null) {
            when (requestStatus) {
                RequestStatus.SEND -> {
                    val request = GetSquadsQuery.Request(
                        __typename = "SquadRequest",
                        id = requestId,
                        user = GetSquadsQuery.User1(
                            __typename = "User",
                            id = currentUserId
                        )
                    )
                    squad.requests.add(request)
                }
                RequestStatus.CANCEL -> {
                    val request = squad.requests.find {
                        it.id == requestId
                    }
                    squad.requests.remove(request)
                }
            }
            val index = squads.indexOf(squad)
            squads[index] = squad
            notifyItemChanged(index)
        }
    }
}