package com.practicum.android.playlistmaker.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.android.playlistmaker.R

class TracksHistoryAdapter(
    private var tracks: MutableList<Track> ,
    private val listener: (Track) -> Unit
) :
    RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        if(position>=0 && position<tracks.size){
            holder.bind(tracks[position])
            holder.itemView.setOnClickListener { listener(tracks[position]) }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}