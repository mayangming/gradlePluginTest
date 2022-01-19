package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient

private const val LOG_TAG = "YM"
class OpenAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_ad)
    }

    override fun onStart() {
        super.onStart()
        loadAd()
//        该代码在没有Google Play的设备上面会崩溃
//        Thread{
//            val adId = AdvertisingIdClient.getAdvertisingIdInfo(this)
//            Log.e("YM---广告Id","--ad:$adId")
//        }.start()

    }

    private fun loadAd(){
        val application = application as? CustomApp
        val openAdaManager = application?.appOpenAdManager
        // If the application is not an instance of MyApplication, log an error message and
        // start the MainActivity without showing the app open ad.
        Log.e(LOG_TAG, "000000:${openAdaManager == null}")
        if (application == null) {
            Log.e(LOG_TAG, "000000")
            return
        }
        // Show the app open ad.
//        application.showAdIfAvailable(
//            this,
//            object : OnShowAdCompleteListener {
//                override fun onShowAdComplete() {
////                    startMainActivity()
//                    Log.e(LOG_TAG, "onShowAdComplete")
//                }
//
//                override fun onLoadError() {
//                    Log.e(LOG_TAG, "onLoadError")
//                }
//            })

        //加载广告
        openAdaManager?.loadAd(this, object : OnAdLoadListener{
            override fun onLoadSuccess() {
                openAdaManager.showAdIfAvailable(this@OpenAdActivity)
                Log.e(LOG_TAG, "success")
            }

            override fun onLoadIng() {
                Log.e(LOG_TAG, "loading")
            }

            override fun onLoadError() {
                Log.e(LOG_TAG, "error")
            }
        })

    }

}