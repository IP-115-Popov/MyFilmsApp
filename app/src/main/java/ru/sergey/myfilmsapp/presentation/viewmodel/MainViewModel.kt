package ru.sergey.myfilmsapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.sergey.domain.model.Film
import ru.sergey.domain.usecase.GetFilmsUseCase

class MainViewModel(
    private val getFilmsUseCase : GetFilmsUseCase
): ViewModel() {

    private var _films = MutableLiveData<List<Film>>()
    val items: LiveData<List<Film>> = _films
    init {
        Log.i("myLog","MainViewModel Created")
        getFilms()
    }
    fun getFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = getFilmsUseCase.execute()
            launch(Dispatchers.Main) { // Обновление UI в главном потоке
                _films.value = items
            }
        }
    }
}