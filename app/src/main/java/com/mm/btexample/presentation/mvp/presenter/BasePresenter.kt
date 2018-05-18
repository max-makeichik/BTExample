package com.mm.btexample.presentation.mvp.presenter

import com.arellomobile.mvp.MvpPresenter
import com.mm.btexample.presentation.mvp.view.BaseMvpView

open class BasePresenter<View : BaseMvpView> : MvpPresenter<View>() {

}