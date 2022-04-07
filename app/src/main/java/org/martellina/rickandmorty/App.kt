package org.martellina.rickandmorty

import android.app.Application
import org.martellina.rickandmorty.network.Repository
import org.martellina.rickandmorty.di.AppComponent
import org.martellina.rickandmorty.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this)

        appComponent = DaggerAppComponent.create()
    }

}