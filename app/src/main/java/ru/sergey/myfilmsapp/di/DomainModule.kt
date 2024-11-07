package ru.sergey.myfilmsapp.di

import org.koin.dsl.module
import ru.sergey.domain.usecase.GetFilmsUseCase

val domainModule = module {
    factory<GetFilmsUseCase> {
        GetFilmsUseCase(repository = get())
    }
}