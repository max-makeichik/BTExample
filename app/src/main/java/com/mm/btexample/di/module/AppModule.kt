package com.mm.btexample.di.module

import android.content.Context
import com.mm.btexample.data.repository.repository_impl.PrefsRepositoryImpl
import com.mm.btexample.data.repository.repository_interface.IPrefsRepository
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class AppModule(context: Context) : Module() {
    init {
        //Global
        bind(Context::class.java).toInstance(context)

        bind(IPrefsRepository::class.java).toInstance(PrefsRepositoryImpl(context))
    }
}
