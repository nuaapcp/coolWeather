package com.test.coolweather.test

import android.util.Log
import javax.inject.Inject

class Truck @Inject constructor() {
    fun deliver() {
        print("Truck is delivering cargo.")
        Log.d("Truck", "deliver:Truck is delivering cargo. ")
    }
}