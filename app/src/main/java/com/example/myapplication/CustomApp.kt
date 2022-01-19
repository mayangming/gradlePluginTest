package com.example.myapplication

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class CustomApp: Application(),Application.ActivityLifecycleCallbacks, LifecycleObserver {

    var appOpenAdManager: AppOpenAdManager ?= null
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        MobileAds.initialize(this) {}
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()

        //添加设备为Admob的测试设备，该id号从logcat获取，没有固定寻找方法，一行一行看日志
        RequestConfiguration.Builder().setTestDeviceIds(listOf("7584E2B43F933E6DB541355C31F8E321","B1405AF9E28EF1773CA7FC9DC20A4412"))
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        appOpenAdManager?.let {
            if (it.isShowingAd){
                currentActivity = activity
            }
        }
        // Show the ad (if available) when the app moves to foreground.
        currentActivity?.let {
            appOpenAdManager?.showAdIfAvailable(it)
        }


    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager?.showAdIfAvailable(activity, onShowAdCompleteListener)
    }


}