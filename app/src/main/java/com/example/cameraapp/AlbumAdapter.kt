package com.example.cameraapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date

class AlbumAdapter(private val onClick: (Album) -> Unit) :
    ListAdapter<Album, AlbumAdapter.AlbumViewHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_item, parent, false)
        return AlbumViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AlbumViewHolder(itemView: View, val onClick: (Album) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val albumNameTextView: TextView = itemView.findViewById(R.id.album_name)
        private val albumDateTextView: TextView = itemView.findViewById(R.id.album_date)
        private var currentAlbum: Album? = null

        init {
            itemView.setOnClickListener {
                currentAlbum?.let {
                    onClick(it)
                }
            }
        }

        fun bind(album: Album) {
            currentAlbum = album
            albumNameTextView.text = album.name
            albumDateTextView.text = SimpleDateFormat("yyyy-MM-dd").format(Date(album.date))
        }
    }
}

class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}
