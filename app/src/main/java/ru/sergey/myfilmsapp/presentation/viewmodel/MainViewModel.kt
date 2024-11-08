package ru.sergey.myfilmsapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
        "Фантастика",
    )

    init {
        Log.i("myLog", "MainViewModel Created")
        getFilms()
    }

    fun getFilms(genresFilter: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val items: List<Film>
            if (genresFilter == null) {
                items = getFilmsUseCase.execute()
            } else {
                val a = getFilmsUseCase.execute()
                items = a.filter {
                    it.genres.any { g ->
                        val genresFilterLow = genresFilter.toLowerCase()
                        g == genresFilterLow
                    }
                }

            }
            Log.i("myLog", "Films is null" + (items == null).toString())
            launch(Dispatchers.Main) {
                _films.value = items
            }
        }
    }
}