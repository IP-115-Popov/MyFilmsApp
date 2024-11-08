package ru.sergey.data.repository

import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sergey.domain.model.Film
import ru.sergey.domain.model.FilmLoadingError
import ru.sergey.domain.repository.FilmRepository


class FilmRepositoryImp : FilmRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://s3-eu-west-1.amazonaws.com/sequeniatesttask/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val productApi = retrofit.create(ProductApi::class.java)


    override suspend fun getFilm(): Either<FilmLoadingError, List<Film>> {
        return try {
            withContext(Dispatchers.IO) {
                val filmResponse = productApi.getFilms()
                filmResponse.films.right() // Возвращаем успешный результат
            }
        } catch (e: Exception) {
            // Обработка ошибок
            Log.e("FilmRepository", "Error fetching films: ${e.message}")
            return FilmLoadingError.left()
        }
    }
}