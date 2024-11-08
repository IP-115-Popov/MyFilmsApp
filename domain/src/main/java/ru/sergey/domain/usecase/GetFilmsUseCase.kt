package ru.sergey.domain.usecase

import arrow.core.Either
import ru.sergey.domain.model.Film
import ru.sergey.domain.model.FilmLoadingError
import ru.sergey.domain.repository.FilmRepository

class GetFilmsUseCase(val repository: FilmRepository) {
    suspend fun execute(): Either<FilmLoadingError, List<Film>> {
        val filmListEither = repository.getFilm()
        return filmListEither
    }
}