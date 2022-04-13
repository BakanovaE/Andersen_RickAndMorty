package org.martellina.rickandmorty

import android.app.Application
import android.content.Context
import org.martellina.rickandmorty.di.AppComponent
import org.martellina.rickandmorty.di.AppModule
import org.martellina.rickandmorty.data.RepositoryImpl
import org.martellina.rickandmorty.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = this))
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }
