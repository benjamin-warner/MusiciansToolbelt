package com.benjaminnwarner.musicianstoolbelt.views.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benjaminnwarner.musicianstoolbelt.database.recording.Recording

class RecordingAdapter(private val list: List<Recording>)
    : RecyclerView.Adapter<RecordingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecordingViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        val recording: Recording = list[position]
        holder.bind(recording)
    }

    override fun getItemCount(): Int = list.size
}
