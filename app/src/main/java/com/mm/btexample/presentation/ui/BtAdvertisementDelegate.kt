package com.mm.btexample.presentation.ui

import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import com.mm.btexample.R
import com.mm.btexample.ext.toast
import com.mm.btexample.util.BlueToothUtil
import com.mm.btexample.util.UuidUtils
import timber.log.Timber
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by Maksim Makeychik on 19.05.2018.
 */
class BtAdvertisementDelegate {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun startAdvertising(context: Context) {
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter == null || !btAdapter.isEnabled) {
            Timber.e("startAdvertising BlueTooth is off")
            return
        }
        if (!BlueToothUtil.isBleAdvertisementSupported(context)) {
            Timber.e(context.getString(R.string.error_device_has_no_ble_support))
            return
        }
        btAdapter.bluetoothLeAdvertiser.startAdvertising(getAdvertiseSettings(),
                getAdvertiseData(), @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
                Timber.d("bluetoothLeAdvertiser success")
                context.toast(context.getString(R.string.advertiser_success))
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                Timber.e("bluetoothLeAdvertiser error $errorCode")
                context.toast(context.getString(R.string.advertiser_error))
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getAdvertiseData(): AdvertiseData? {
        val mBuilder = AdvertiseData.Builder()
        val mManufacturerData = ByteBuffer.allocate(24)
        val uuidString = "beelme124786testforartem"
        val uuid = UuidUtils.asBytes(UUID.nameUUIDFromBytes(uuidString.toByteArray()))
        mManufacturerData.put(0, 0xBE.toByte()) // Beacon Identifier
        mManufacturerData.put(1, 0xAC.toByte()) // Beacon Identifier
        for (i in 2..17) {
            mManufacturerData.put(i, uuid[i - 2]) // adding the UUID
        }
        mManufacturerData.put(18, 0x01.toByte())// first byte of Major
        mManufacturerData.put(19, 0x04.toByte())// second byte of Major
        mManufacturerData.put(20, 0x07.toByte())// first minor
        mManufacturerData.put(21, 0x03.toByte())// second minor
        mManufacturerData.put(22, 0xB5.toByte())// txPower
        mBuilder.addManufacturerData(224, mManufacturerData.array()) // using google's company ID
        return mBuilder.build()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getAdvertiseSettings(): AdvertiseSettings? {
        val mBuilder = AdvertiseSettings.Builder()
        mBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
        mBuilder.setConnectable(false)
        mBuilder.setTimeout(0)
        mBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
        return mBuilder.build()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun stopAdvertising(context: Context) {
        if (!BlueToothUtil.isBleAdvertisementSupported(context)) {
            return
        }
        BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser.stopAdvertising(@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
                Timber.i("bluetoothLeAdvertiser stop success")
                context.toast(context.getString(R.string.advertiser_stop_success))
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                Timber.e("bluetoothLeAdvertiser stop error $errorCode")
            }
        })
    }
}
