package com.mm.btexample.presentation.mvp.view

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface MainMvpView : BaseMvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setBlueToothSwitchFromPrefs(btSwitchState: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setBlueToothSwitchCheckAuto()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun switchBlueTooth(enabled: Boolean)

}
