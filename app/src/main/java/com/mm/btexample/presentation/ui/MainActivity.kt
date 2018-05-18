package com.mm.btexample.presentation.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.CompoundButton
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.mm.btexample.R
import com.mm.btexample.di.DI
import com.mm.btexample.ext.toast
import com.mm.btexample.presentation.mvp.presenter.MainPresenter
import com.mm.btexample.presentation.mvp.view.MainMvpView
import com.mm.btexample.presentation.ui.base.BaseActivity
import com.mm.btexample.util.UuidUtils.asBytes
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import toothpick.Toothpick
import java.nio.ByteBuffer
import java.util.*

class MainActivity : BaseActivity(), MainMvpView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return Toothpick
                .openScopes(DI.APP_SCOPE)
                .getInstance(MainPresenter::class.java)
    }

    private val btListener = getBtListener()

    private fun getBtListener(): (CompoundButton, Boolean) -> Unit {
        return { _: CompoundButton, isChecked: Boolean ->
            presenter.onBlueToothSwitchChecked(isChecked)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbar()
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (btAdapter == null) {
            showError(getString(R.string.error_device_does_not_support_bluetooth))
            bluetooth_switch.isEnabled = false
            return
        } else {
            bluetooth_switch.isChecked = btAdapter.isEnabled
        }
        bluetooth_switch.setOnCheckedChangeListener(btListener)
        registerReceiver(btReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        if (!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported ||
                BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser == null) {
            showError(getString(R.string.error_device_has_no_ble_support))
            Timber.e(getString(R.string.error_device_has_no_ble_support))
            return
        }
        BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser.startAdvertising(getAdvertiseSettings(),
                getAdvertiseData(), object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
                Timber.d("bluetoothLeAdvertiser success")
                toast(getString(R.string.advertiser_success))
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                Timber.e("bluetoothLeAdvertiser error $errorCode")
                showError(getString(R.string.advertiser_error))
            }
        })
    }

    private fun getAdvertiseData(): AdvertiseData? {
        val mBuilder = AdvertiseData.Builder()
        val mManufacturerData = ByteBuffer.allocate(24)
        val uuidString = "beelme124786testforartem"
        val uuid = asBytes(UUID.nameUUIDFromBytes(uuidString.toByteArray()))
        mManufacturerData.put(0, 0xBE.toByte()) // Beacon Identifier
        mManufacturerData.put(1, 0xAC.toByte()) // Beacon Identifier
        for (i in 2..17) {
            mManufacturerData.put(i, uuid[i - 2]); // adding the UUID
        }
        mManufacturerData.put(18, 0x01.toByte())// first byte of Major
        mManufacturerData.put(19, 0x04.toByte())// second byte of Major
        mManufacturerData.put(20, 0x07.toByte())// first minor
        mManufacturerData.put(21, 0x03.toByte())// second minor
        mManufacturerData.put(22, 0xB5.toByte())// txPower
        mBuilder.addManufacturerData(224, mManufacturerData.array()) // using google's company ID
        return mBuilder.build()
    }

    private fun getAdvertiseSettings(): AdvertiseSettings? {
        val mBuilder = AdvertiseSettings.Builder()
        mBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
        mBuilder.setConnectable(false)
        mBuilder.setTimeout(0)
        mBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
        return mBuilder.build()
    }

    override fun switchBlueTooth(enabled: Boolean) {
        if (enabled) {
            BluetoothAdapter.getDefaultAdapter().enable()
        } else {
            BluetoothAdapter.getDefaultAdapter().disable()
        }
    }

    override fun setBlueToothSwitchCheckAuto() {
        val btAdapter = BluetoothAdapter.getDefaultAdapter()
        btAdapter?.let {
            setBtSwitchChecked(btAdapter.isEnabled)
        } ?: run {
            setBtSwitchChecked(false)
        }
    }

    override fun setBlueToothSwitchFromPrefs(btSwitchState: Boolean) {
        setBtSwitchChecked(btSwitchState)
        switchBlueTooth(btSwitchState)
    }

    private fun setBtSwitchChecked(btSwitchState: Boolean) {
        bluetooth_switch.setOnCheckedChangeListener(null)
        bluetooth_switch.isChecked = btSwitchState
        bluetooth_switch.setOnCheckedChangeListener(btListener)
    }

    private val btReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        presenter.onBlueToothStateChanged(false)
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                    }
                    BluetoothAdapter.STATE_ON -> {
                        presenter.onBlueToothStateChanged(true)
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                    }
                }
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(btReceiver)
        } catch (ignored: Exception) {
        }
    }
}
