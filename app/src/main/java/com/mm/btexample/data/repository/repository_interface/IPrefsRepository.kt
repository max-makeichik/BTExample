package com.mm.btexample.data.repository.repository_interface

interface IPrefsRepository {

    /**
     * Bluetooth switch state
     *
     * @return true - if check, false - otherwise
     */
    var btSwitchState: Boolean

    fun hasBtSwitchState(): Boolean

}
