package ru.sergey.data.model

import ru.sergey.domain.model.Film

data class FilmResponse(
    val films: List<Film>
)

