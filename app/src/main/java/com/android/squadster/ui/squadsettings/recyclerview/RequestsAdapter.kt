package com.android.squadster.ui.squadsettings.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.R
import com.android.squadster.model.data.server.model.Request

class RequestsAdapter(
    private val handler: OnClickRequest
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var requests = ArrayList<Request>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)

        return RequestViewHolder(itemView, handler)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RequestViewHolder).bindItem(requests[position])
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    fun setData(listOfRequests: ArrayList<Request>?) {
        if (listOfRequests != null) {
            val notApprovedRequests = listOfRequests.filter {
                it.approvedAt == null || it.approvedAt.toString().isEmpty()
            }
            if (notApprovedRequests.isEmpty()) {
                handler.requestsListIsEmpty()
            } else {
                requests = ArrayList(notApprovedRequests)
                notifyDataSetChanged()
            }
        }
    }

    fun deleteRequest(id: String) {
        val request = requests.find {
            it.id == id
        }
        if (request != null) {
            val index = requests.indexOf(request)
            requests.remove(request)
            notifyItemRemoved(index)
        }
        if (requests.isEmpty()) {
            handler.requestsListIsEmpty()
        }
    }
}