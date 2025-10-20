package expo.modules.tradplusad

import android.util.Log
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.interstitial.InterstitialAdListener
import com.tradplus.ads.open.interstitial.TPInterstitial
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.tradplusad.utils.Utils

class ExpoInterstitialAdModule: Module() {

    private val adArray = HashMap<String, TPInterstitial?>()

    override fun definition() = ModuleDefinition {
        Name("ExpoInterstitialAd")

        Events(
            "onAdLoaded",
            "onAdClicked",
            "onAdImpression",
            "onAdFailed",
            "onAdClosed",
            "onAdVideoStart",
            "onAdVideoEnd",
            "onAdVideoError"
        )

        Function("initAd") { placementId: String ->
            getOrCreateRewardAd(placementId)
            placementId
        }


        Function("loadAd") { placementId: String ->
            val ad = getOrCreateRewardAd(placementId)
            ad.loadAd()
        }

        Function("reloadAd") { placementId: String ->
            val ad = getOrCreateRewardAd(placementId)
            ad.reloadAd()
        }

        Function("entryAdScenario") { placementId: String, entryAdScenario: String ->
            val ad = getOrCreateRewardAd(placementId)
            ad.entryAdScenario(entryAdScenario)
        }


        Function("isReady") { placementId: String ->
            val ad = getOrCreateRewardAd(placementId)
            ad.isReady
        }

        Function("showAd") { placementId: String ->
            val ad = getOrCreateRewardAd(placementId)
            ad.showAd(context, null)
        }

        Function("onDestroy") { placementId: String ->
            val ad = adArray[placementId]
            if (ad != null) {
                ad.setAdListener(null)
                ad.onDestroy()
                adArray.remove(placementId)
                Log.d(TAG, "Destroyed ad for placementId=$placementId")
            } else {
                Log.w(TAG, "Attempted to destroy non-existent ad for placementId=$placementId")
            }
        }

        OnActivityDestroys {
            Log.d(TAG, "OnActivityDestroys called, cleaning up all ads")
            clearAllAds()
        }

        OnDestroy {
            Log.d(TAG, "OnDestroy called, cleaning up all ads")
            clearAllAds()
        }

    }

    private fun getOrCreateRewardAd(placementId: String): TPInterstitial {
        adArray[placementId]?.let { return it }

        Log.d(TAG, "Creating new TPReward for placementId=$placementId")

        val tpInterstitial = TPInterstitial(context, placementId).apply {
            setAdListener(object : InterstitialAdListener {
                override fun onAdLoaded(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdLoaded", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdClicked(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdClicked", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdImpression(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdImpression", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdFailed(tpAdError: TPAdError?) {
                    sendEvent("onAdFailed", mapOf(
                        "placementId" to placementId,
                        "tpAdError" to Utils.mapToAdError(tpAdError)
                    ))
                }

                override fun onAdClosed(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdClosed", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdVideoStart(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdVideoStart", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdVideoEnd(tpAdInfo: TPAdInfo?) {
                    sendEvent("onAdVideoEnd", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                    ))
                }

                override fun onAdVideoError(tpAdInfo: TPAdInfo?, tpAdError: TPAdError?) {
                    sendEvent("onAdVideoError", mapOf(
                        "placementId" to placementId,
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                        "tpAdError" to Utils.mapToAdError(tpAdError)
                    ))
                }
            })
        }

        adArray[placementId] = tpInterstitial
        return tpInterstitial
    }

    private fun clearAllAds() {
        for ((placementId, ad) in adArray) {
            ad?.setAdListener(null)
            ad?.onDestroy()
            Log.d(TAG, "Cleared ad for placementId=$placementId")
        }
        adArray.clear()
    }

    companion object {
        private const val TAG = "ExpoInterstitialAd"
    }

    private val context
        get() = requireNotNull(appContext.throwingActivity)
}
