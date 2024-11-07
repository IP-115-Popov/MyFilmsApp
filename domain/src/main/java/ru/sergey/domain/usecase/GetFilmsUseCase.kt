package ru.sergey.domain.usecase

import ru.sergey.domain.model.Film
import ru.sergey.domain.repository.FilmRepository

class GetFilmsUseCase(val repository: FilmRepository) {
    suspend fun execute() : List<Film> {
        return repository.getFilm()
    }
}