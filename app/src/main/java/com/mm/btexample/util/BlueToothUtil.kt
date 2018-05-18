package com.mm.btexample.util

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by Maksim Makeychik on 19.05.2018.
 */
object BlueToothUtil {

    fun isBleAdvertisementSupported(context: Context): Boolean {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false
        }
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported &&
                BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser != null
    }
}