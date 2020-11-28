package com.android.squadster.ui.squadsettings.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.squadster.model.data.server.model.Request
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_request.view.*

class RequestViewHolder(
    view: View,
    private val handler: OnClickRequest
) : RecyclerView.ViewHolder(view) {

    private lateinit var request: Request

    fun bindItem(request: Request) {
        this.request = request

        setupViews()
    }

    private fun setupViews() {
        loadRequestAvatar()
        itemView.tv_request_username.text = "${request.user?.firstName}  ${request.user?.lastName}"
        itemView.iv_accept_request.setOnClickListener {
            handler.acceptRequest(request.id)
        }
        itemView.iv_reject_request.setOnClickListener {
            handler.rejectRequest(request.id)
        }
    }

    private fun loadRequestAvatar() {
        Glide.with(itemView.context)
            .load(request.user?.smallImageUrl)
            .circleCrop()
            .into(itemView.iv_request_avatar)
    }
}
