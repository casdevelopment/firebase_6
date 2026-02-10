package com.example.esm.network.koin_module

import android.app.Application
import androidx.annotation.Keep
import com.example.esm.network.koin_module.networkModule
import com.example.esm.network.koin_module.repoModule
import com.example.esm.network.koin_module.sharedPreference
import com.example.esm.network.koin_module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
@Keep
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(networkModule, repoModule, viewModelModule, sharedPreference))
        }
    }
}