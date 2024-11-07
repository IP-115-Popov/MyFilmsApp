package ru.sergey.myfilmsapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.sergey.myfilmsapp.presentation.viewmodel.MainViewModel

val appModule = module {
    viewModel<MainViewModel>{
        MainViewModel(
            getFilmsUseCase = get(),
        )
    }
}