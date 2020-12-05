package com.android.squadster.ui.squadsettings.recyclerview

interface OnClickRequest {

    fun acceptRequest(id: String)

    fun rejectRequest(id: String)

    fun requestsListIsEmpty()
}