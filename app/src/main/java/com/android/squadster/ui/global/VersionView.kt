package com.android.squadster.ui.global

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.android.squadster.BuildConfig
import com.android.squadster.R

class VersionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        gravity = Gravity.CENTER
        val versionName = resources.getString(
            R.string.debug_version_code,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        )
        text = versionName
    }
}