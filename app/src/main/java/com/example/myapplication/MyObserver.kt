package com.example.myapplication

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class MyObserver : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        Log.e("YM--->Observer","---->onResume()")
    }

    override fun onPause(owner: LifecycleOwner) {
    }

}