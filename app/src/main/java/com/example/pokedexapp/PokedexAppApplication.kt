package com.example.pokedexapp

import android.app.Application
import com.example.pokedexapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class PokedexAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@PokedexAppApplication)
        }
    }
}