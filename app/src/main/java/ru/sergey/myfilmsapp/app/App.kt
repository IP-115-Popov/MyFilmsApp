package ru.sergey.myfilmsapp.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.sergey.myfilmsapp.di.appModule
import ru.sergey.myfilmsapp.di.dataModule
import ru.sergey.myfilmsapp.di.domainModule

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }
}