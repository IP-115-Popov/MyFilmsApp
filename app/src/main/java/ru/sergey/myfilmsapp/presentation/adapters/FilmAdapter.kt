package ru.sergey.myfilmsapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.sergey.domain.model.Film
import ru.sergey.myfilmsapp.R
import ru.sergey.myfilmsapp.databinding.FilmItemBinding

class FilmAdapter(
    var items: List<Film>,
    val noItemClick: (Long)->Unit
) : RecyclerView.Adapter<FilmAdapter.MyViewHolder>() {

    fun replaceAllFilm(films: List<Film>) {
        items = films
        notifyDataSetChanged()
    }

    fun addFilm(film: Film) {
        items = items + film
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View, val noItemClick: (Long)->Unit) : RecyclerView.ViewHolder(view) {
        val binding = FilmItemBinding.bind(view)
        fun bind(film: Film) = with(binding) {
            //отобразить картинку
            tvTitle.text = film.name
            root.setOnClickListener {
                noItemClick(film.id)
                Log.i("myLog", tvTitle.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
        return MyViewHolder(view, noItemClick)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = with(holder) {
        holder.bind(items.get(position))
    }
}