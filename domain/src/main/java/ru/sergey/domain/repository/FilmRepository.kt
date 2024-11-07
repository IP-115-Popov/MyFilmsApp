package ru.sergey.domain.repository

import ru.sergey.domain.model.Film

interface FilmRepository {
    suspend fun getFilm() : List<Film>
}