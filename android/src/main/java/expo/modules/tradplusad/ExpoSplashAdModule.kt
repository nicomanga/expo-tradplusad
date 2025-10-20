package expo.modules.tradplusad

import android.util.Log
import android.widget.FrameLayout
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.base.bean.TPBaseAd
import com.tradplus.ads.open.splash.SplashAdListener
import com.tradplus.ads.open.splash.TPSplash
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.tradplusad.utils.Utils

class ExpoSplashAdModule : Module() {

    private var tpSplash: TPSplash? = null

    private val context
        get() = requireNotNull(appContext.throwingActivity)

    companion object {
        private const val TAG = "ExpoSplashAd"
    }

    override fun definition() = ModuleDefinition {
        Name("ExpoSplashAd")

        Events(
            "onAdLoaded",
            "onAdClicked",
            "onAdClosed",
            "onAdImpression",
            "onAdLoadFailed",
            "onAdShowFailed",
            "onZoomOutEnd",
            "onZoomOutStart"
        )

        Function("initAd") { placementId: String ->
            Log.d(TAG, "Initializing Splash Ad with placementId=$placementId")
            val ad = TPSplash(context, placementId)
            ad.setAdListener(object : SplashAdListener() {
                override fun onAdLoaded(tpAdInfo: TPAdInfo?, tpBaseAd: TPBaseAd?) {
                    sendEvent("onAdLoaded", mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                        "tpBaseAd" to Utils.mapToAdBase(tpBaseAd)
                    ))
                }

                override fun onAdShowFailed(tpAdInfo: TPAdInfo?, tpAdError: TPAdError?) {
                    sendEvent("onAdShowFailed", mapOf(
                        "tpAdError" to Utils.mapToAdError(tpAdError),
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onZoomOutStart(tpAdInfo: TPAdInfo?) {
                    sendEvent("onZoomOutStart", mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onZoomOutEnd(tpAdInfo: TPAdInfo?) {
                    sendEvent("onZoomOutEnd", mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdLoadFailed(tpAdError: TPAdError?) {
                    sendEvent("onAdLoadFailed", mapOf(
                        "tpAdError" to Utils.mapToAdError(tpAdError)
                    ))
                }

                override fun onAdImpression(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdImpression", mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdClosed(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdClosed", mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdClicked(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdClicked", mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }
            })

            tpSplash = ad
            placementId
        }

        Function("loadAd") {
            tpSplash?.loadAd(null)
        }

        Function("entryAdScenario") { entryAdScenario: String ->
            tpSplash?.entryAdScenario(entryAdScenario)
        }

        Function("isReady") {
            return@Function tpSplash?.isReady
        }

        Function("onClean") {
            tpSplash?.onClean()
            Log.d(TAG, "Splash Ad cache cleaned")
        }

        Function("showAd") {
            context.runOnUiThread {
                val ad = tpSplash
                if (ad != null) {
                    try {
                        val layout = FrameLayout(context).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                        }
                        ad.showAd(layout)
                        Log.d(TAG, "Splash Ad displayed")
                    } catch (e: Exception) {
                        Log.e(TAG, "Error showing Splash Ad", e)
                    }
                }
            }
        }

        Function("onDestroy") {
            cleanupSplashAd()
        }

        OnDestroy {
            Log.d(TAG, "Module OnDestroy called")
            cleanupSplashAd()
        }

        OnActivityDestroys {
            Log.d(TAG, "Activity destroyed, cleaning up Splash Ad")
            cleanupSplashAd()
        }
    }

    private fun cleanupSplashAd() {
        tpSplash?.let { ad ->
            ad.setAdListener(null)
            ad.onDestroy()
            Log.d(TAG, "Splash Ad destroyed and listener cleared")
        }
        tpSplash = null
    }
}
