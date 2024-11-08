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

    private val _isLoadingFailed = MutableStateFlow<Boolean>(false)  // Состояние для ошибки
    val isLoadingFailed: StateFlow<Boolean> get() = _isLoadingFailed

    private val _isLoading = MutableStateFlow<Boolean>(true)  // Состояние для ошибки
    val isLoading: StateFlow<Boolean> get() = _isLoading

    val genres = listOf(
        "Биография",
        "Боевик",
        "Детектив",
        "Драма",
        "Комедия",
        "Криминал",
        "Мелодрама",
        "Мюзикл",
        "Приключения",
        "Триллер",
        "Ужасы",
        "Фантастика"
    )

    init {
        Log.i("myLog", "MainViewModel Created")
        getFilms()
    }

    fun getFilms(genresFilter: String? = null) {

        _isLoading.value = true
        var items: List<Film> = emptyList()

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
                            // Обновляем список фильмов
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
}