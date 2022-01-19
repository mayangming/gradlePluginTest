package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.*

//private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/3419835294"
private const val AD_UNIT_ID = "ca-app-pub-1714417130889949/3158301790"
private const val LOG_TAG = "YM"

/** Inner class that loads and shows app open ads. */
class AppOpenAdManager {
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false

    /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
    private var loadTime: Long = 0

    /**
     * Load an ad.
     *
     * @param context the context of the activity that loads the ad
     */
    fun loadAd(context: Context, loadListener: OnAdLoadListener ?= null) {
        // Do not load ad if there is an unused ad or one is already loading.
        Log.e(LOG_TAG, "-----")
        if (isLoadingAd) {
            loadListener?.onLoadIng()
            return
        }

        if (isAdAvailable()){ //广告可用且在有效期内
            loadListener?.onLoadSuccess()
            return
        }

        isLoadingAd = true
        val request = AdRequest.Builder().build()
        Log.e(LOG_TAG, "-->开屏广告ID:$AD_UNIT_ID")
        AppOpenAd.load(
            context,
            AD_UNIT_ID,
            request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    Log.e(LOG_TAG, "onAdLoaded.")
//                    Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show()
                    loadListener?.onLoadSuccess()
                }

                /**
                 * Called when an app open ad has failed to load.
                 *
                 * @param loadAdError the error.
                 */
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                    Log.e(LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
//                    Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show()
                    loadListener?.onLoadError()
                }

            })
    }

    /** Check if ad was loaded more than n hours ago. */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Check if ad exists and can be shown. */
    private fun isAdAvailable(): Boolean {
        // Ad references in the app open beta will time out after four hours, but this time limit
        // may change in future beta versions. For details, see:
        // https://support.google.com/admob/answer/9341964?hl=en
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     */
    fun showAdIfAvailable(activity: Activity) {
        showAdIfAvailable(
            activity,
            object : OnShowAdCompleteListener {
                override fun onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }

                override fun onLoadError() {
                }
            })
    }

    /**
     * Show the ad if one isn't already showing.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(
        activity: Activity,
        onShowAdCompleteListener: OnShowAdCompleteListener
    ) {
        // If the app open ad is already showing, do not show the ad again.
        if (isShowingAd) {
            Log.e(LOG_TAG, "The app open ad is already showing.")
            return
        }

        // If the app open ad is not available yet, invoke the callback then load the ad.
        if (!isAdAvailable()) {
            Log.e(LOG_TAG, "The app open ad is not ready yet.")
            onShowAdCompleteListener.onLoadError()
//            loadAd(activity)
            return
        }

        Log.e(LOG_TAG, "Will show ad.")

        appOpenAd!!.setFullScreenContentCallback(
            object : FullScreenContentCallback() {
                /** Called when full screen content is dismissed. */
                override fun onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    appOpenAd = null
                    isShowingAd = false
                    Log.e(LOG_TAG, "onAdDismissedFullScreenContent.")
//                    Toast.makeText(activity, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show()
                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)//广告消失后继续加载下一则广告
                }

                /** Called when fullscreen content failed to show. */
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    Log.e(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.message)
//                    Toast.makeText(activity, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT).show()

                    onShowAdCompleteListener.onLoadError()
                    loadAd(activity)
                }

                /** Called when fullscreen content is shown. */
                override fun onAdShowedFullScreenContent() {
                    Log.e(LOG_TAG, "onAdShowedFullScreenContent.")
//                    Toast.makeText(activity, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT).show()
                }
            })
        isShowingAd = true
        appOpenAd!!.show(activity)
    }
}