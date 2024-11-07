package ru.sergey.data.repository

import retrofit2.http.GET
import ru.sergey.data.model.FilmResponse

interface ProductApi {
    @GET("films.json")
    suspend fun getFilms(): FilmResponse
}