package com.mm.btexample.presentation.ui

import android.bluetooth.BluetoothAdapter
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
import com.mm.btexample.presentation.mvp.presenter.MainPresenter
import com.mm.btexample.presentation.mvp.view.MainMvpView
import com.mm.btexample.presentation.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import toothpick.Toothpick

class MainActivity : BaseActivity(), MainMvpView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return Toothpick
                .openScopes(DI.APP_SCOPE)
                .getInstance(MainPresenter::class.java)
    }

    private val btAdvertisementDelegate = BtAdvertisementDelegate()
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
        btAdvertisementDelegate.startAdvertising(this)
        registerReceiver(btReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
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
                        btAdvertisementDelegate.stopAdvertising(this@MainActivity)
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                    }
                    BluetoothAdapter.STATE_ON -> {
                        presenter.onBlueToothStateChanged(true)
                        btAdvertisementDelegate.startAdvertising(this@MainActivity)
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
        btAdvertisementDelegate.stopAdvertising(this)
    }
}
