package ru.sergey.myfilmsapp.di

import org.koin.dsl.module
import ru.sergey.data.repository.FilmRepositoryImp
import ru.sergey.domain.repository.FilmRepository

val dataModule = module {
    single<FilmRepository>
    {
        FilmRepositoryImp()
    }
}