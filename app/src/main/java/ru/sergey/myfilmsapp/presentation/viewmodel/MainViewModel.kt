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

    private val _filmsForShow = MutableStateFlow<List<Film>>(mutableListOf())
    val filmsForShow: StateFlow<List<Film>>
        get() {
            _filmsForShow.value = _filmsForShow.value.sortedBy { it.localized_name }
            return _filmsForShow.asStateFlow()
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

    var items: List<Film> = emptyList()

    init {
        Log.i("myLog", "MainViewModel Created")
        downloadFilms()
    }


    fun downloadFilms() {
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

                            items = filmListEither.value

                            //Обновляем список жанров и фильмов
                            updateGenres()
                            updateFilms()

                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoadingFailed.value = true
                    _isLoading.value = false
                    _filmsForShow.value = emptyList() // Очистить список фильмов при ошибке
                }
            }
        }
    }


    fun updateGenres() {
        _genres.value = items.flatMap { it.genres }.distinct()
    }

    fun updateFilms() {
        val genresFilter: String? = selectedGenre.value
        if (genresFilter == null) {
            _filmsForShow.value = items
        } else {
            _filmsForShow.value = items.filter {
                it.genres.any { g ->
                    val genresFilterLow = genresFilter.toLowerCase()
                    g == genresFilterLow
                }
            }
        }
    }
}