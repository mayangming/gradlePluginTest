package com.example.myapplication

/**
 * Interface definition for a callback to be invoked when an app open ad is complete
 * (i.e. dismissed or fails to show).
 */
interface OnShowAdCompleteListener {
    fun onShowAdComplete()
    fun onLoadError()
}

interface OnAdLoadListener {
    fun onLoadSuccess()
    fun onLoadIng()
    fun onLoadError()
}