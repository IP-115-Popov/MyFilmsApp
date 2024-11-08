package ru.sergey.domain.repository

import arrow.core.Either
import ru.sergey.domain.model.Film
import ru.sergey.domain.model.FilmLoadingError

interface FilmRepository {
    suspend fun getFilm() : Either<FilmLoadingError, List<Film>>
}