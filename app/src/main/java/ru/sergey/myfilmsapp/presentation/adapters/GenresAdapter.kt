package ru.sergey.myfilmsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GenresAdapter(
    private val genres: List<String>,
    private val onGenreClick: (String) -> Unit
) : RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val genreTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(genre: String) {
            genreTextView.text = genre
            itemView.setOnClickListener { onGenreClick(genre) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return GenreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres.get(position))
    }

    override fun getItemCount() = genres.count()
}