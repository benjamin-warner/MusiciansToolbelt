package com.benjaminnwarner.musicianstoolbelt.views.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording

class RecordingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_recording, parent, false)) {
    private var id: TextView? = null
    private var filename: TextView? = null

    init {
        id = itemView.findViewById(R.id.recording_id)
        filename = itemView.findViewById(R.id.recording_filename)

    }

    fun bind(recording: Recording) {
        id?.text = recording.id.toString()
        filename?.text = recording.filename
    }

}