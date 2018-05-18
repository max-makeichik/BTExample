package com.mm.btexample.presentation.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.mm.btexample.data.repository.repository_interface.IPrefsRepository
import com.mm.btexample.presentation.mvp.view.MainMvpView
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val prefsRepository: IPrefsRepository
) : BasePresenter<MainMvpView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (prefsRepository.hasBtSwitchState()) {
            viewState.setBlueToothSwitchFromPrefs(prefsRepository.btSwitchState)
        } else {
            viewState.setBlueToothSwitchCheckAuto()
        }
    }

    fun onBlueToothSwitchChecked(checked: Boolean) {
        prefsRepository.btSwitchState = checked
        viewState.switchBlueTooth(checked)
    }

    fun onBlueToothStateChanged(enabled: Boolean) {
        prefsRepository.btSwitchState = enabled
        viewState.setBlueToothSwitchCheckAuto()
    }

}
