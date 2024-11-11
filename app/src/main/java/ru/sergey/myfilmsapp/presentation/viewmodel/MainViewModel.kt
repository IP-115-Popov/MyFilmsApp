package ru.sergey.myfilmsapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.sergey.domain.model.Film
import ru.sergey.domain.usecase.GetFilmsUseCase

class MainViewModel(
    private val getFilmsUseCase: GetFilmsUseCase
) : ViewModel() {

    private val _films = MutableStateFlow<List<Film>>(mutableListOf())
    val films: StateFlow<List<Film>>
        get() {
            _films.value = _films.value.sortedBy { it.localized_name }
            return _films.asStateFlow()
        }

    private val _genres = MutableStateFlow<List<String>>(mutableListOf())
    val genres: StateFlow<List<String>> = _genres

    private val _isLoadingFailed = MutableStateFlow<Boolean>(false)
    val isLoadingFailed: StateFlow<Boolean> get() = _isLoadingFailed

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> get() = _selectedGenre
    fun setSelectedGenre(value: String?) {
        _selectedGenre.value = value
    }

    init {
        Log.i("myLog", "MainViewModel Created")
        getFilms()
    }

    fun getFilms() {
        val genresFilter: String? = selectedGenre.value
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val filmListEither = getFilmsUseCase.execute()

                withContext(Dispatchers.Main) {
                    when (filmListEither) {
                        is Either.Left -> {
                            // Ошибка при загрузке данных
                            _isLoadingFailed.value = true
                            _isLoading.value = false
                        }

                        is Either.Right -> {
                            // Успешная загрузка данных
                            _isLoadingFailed.value = false
                            _isLoading.value = false

                            val items: List<Film> = filmListEither.value

                            //Обновляем список жанров и фильмов
                            updateGenres(items)
                            updateFilms(genresFilter, items)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoadingFailed.value = true
                    _isLoading.value = false
                    _films.value = emptyList() // Очистить список фильмов при ошибке
                }
            }
        }
    }

    private fun updateGenres(items: List<Film>) {
        _genres.value = items.flatMap { it.genres }.distinct()
    }

    private fun updateFilms(
        genresFilter: String?,
        items: List<Film>
    ) {
        if (genresFilter == null) {
            _films.value = items
        } else {
            _films.value = items.filter {
                it.genres.any { g ->
                    val genresFilterLow = genresFilter.toLowerCase()
                    g == genresFilterLow
                }
            }
        }
    }
}