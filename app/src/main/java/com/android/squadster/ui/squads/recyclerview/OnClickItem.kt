package com.android.squadster.ui.squads.recyclerview

interface OnClickItem {

    fun sendRequest(squadId: String)

    fun cancelRequest(requestId: String?)
}