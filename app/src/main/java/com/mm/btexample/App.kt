package com.mm.btexample

import android.app.Application
import com.mm.btexample.di.DI
import com.mm.btexample.di.module.AppModule
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
        MemberInjectorRegistryLocator.setRootRegistry(com.mm.btexample.MemberInjectorRegistry())
        FactoryRegistryLocator.setRootRegistry(com.mm.btexample.FactoryRegistry())

        val appScope = Toothpick.openScope(this)
        initToothpick(appScope)
        initAppScope()
    }

    private fun initToothpick(appScope: Scope) {
        appScope.installModules(AppModule(this))
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))
    }

}
