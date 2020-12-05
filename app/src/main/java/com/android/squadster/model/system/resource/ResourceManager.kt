package com.android.squadster.model.system.resource

import android.content.Context
import javax.inject.Inject

class ResourceManager @Inject constructor(
    private val context: Context
) {

    fun getString(id: Int) = context.getString(id)

    fun getString(id: Int, vararg formatArgs: Any): String {
        return String.format(context.getString(id, *formatArgs))
    }
}