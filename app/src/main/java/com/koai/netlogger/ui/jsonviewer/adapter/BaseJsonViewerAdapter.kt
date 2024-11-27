package com.koai.netlogger.ui.jsonviewer.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseJsonViewerAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {
    abstract fun expandAll()

    abstract fun collapseAll()

    companion object {
        var KEY_COLOR: Int = -0x6dd867
        var TEXT_COLOR: Int = -0xc54ab6
        var NUMBER_COLOR: Int = -0xda551e
        var BOOLEAN_COLOR: Int = -0x67d80
        var URL_COLOR: Int = -0x992d2b
        var NULL_COLOR: Int = -0x10a6cb
        var BRACES_COLOR: Int = -0xb5aaa1

        var TEXT_SIZE_DP: Float = 12f
    }
}
