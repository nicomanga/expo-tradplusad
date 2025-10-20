package expo.modules.tradplusad.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.banner.BannerAdListener
import com.tradplus.ads.open.banner.TPBanner
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import expo.modules.tradplusad.common.BannerAdView
import expo.modules.tradplusad.utils.Utils

@SuppressLint("ViewConstructor")
class BannerView(context: Context, appContext: AppContext): ExpoView(context, appContext) {

    val onAdLoaded by EventDispatcher()
    val onAdClicked by EventDispatcher()
    val onAdClosed by EventDispatcher()
    val onAdImpression by EventDispatcher()
    val onAdLoadFailed by EventDispatcher()
    val onAdShowFailed by EventDispatcher()
    val onBannerRefreshed by EventDispatcher()

    val onReady by EventDispatcher()

    private val reactContext get() = appContext.reactContext ?: throw Exceptions.AppContextLost()

    internal val main = BannerAdView(context)

    init {
        val matchParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        main.layoutParams = matchParams
        addView(main, matchParams)
    }

    private fun getAdView(): TPBanner? {
        return main.getChildAt(0) as TPBanner?
    }

    private fun initAdView(): TPBanner? {

        val oldAdView = getAdView()

        if (oldAdView != null) {
            oldAdView.setAdListener(null)
            oldAdView.onDestroy()
            main.removeView(oldAdView)
        }

        val adView = TPBanner(reactContext)
        adView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS)


        adView.setAdListener(object : BannerAdListener() {
            override fun onAdLoaded(tpAdInfo: TPAdInfo) {
                onAdLoaded(mapOf(
                    "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                ))
            }

            override fun onAdLoadFailed(tpAdError: TPAdError?) {
                onAdLoadFailed(mapOf(
                    "tpAdError" to Utils.mapToAdError(tpAdError)
                ))
            }

            override fun onAdShowFailed(tpAdError: TPAdError?, tpAdInfo: TPAdInfo?) {
                onAdShowFailed(mapOf(
                    "tpAdError" to Utils.mapToAdError(tpAdError),
                    "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                ))
            }

            override fun onAdImpression(tpAdInfo: TPAdInfo?) {
                onAdImpression(mapOf(
                    "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                ))
            }

            override fun onAdClosed(tpAdInfo: TPAdInfo) {
                onAdClosed(mapOf(
                    "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                ))
            }

            override fun onAdClicked(tpAdInfo: TPAdInfo) {
                onAdClicked(mapOf(
                    "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo)
                ))
            }

            override fun onBannerRefreshed() {
                onBannerRefreshed(mapOf(
                    "status" to true
                ))
            }
        })

        if (adView.parent != null) {
            (adView.parent as ViewGroup).removeView(adView)
        }

        main.addView(adView)

        return adView
    }

    internal fun setPlacementId(placementId: String) {
        main.setPlacementId(placementId)
        main.setPropsChanged(true)
    }

    internal fun setEntryAdScenario(entryAdScenario: String) {
        main.setEntryAdScenario(entryAdScenario)
        main.setPropsChanged(true)
    }

    internal fun setAutoDestroy(autoDestroy: Boolean) {
        main.setAutoDestroy(autoDestroy)
        main.setPropsChanged(true)
    }

    internal fun setCloseAutoShow(closeAutoShow: Boolean) {
        main.setCloseAutoShow(closeAutoShow)
        main.setPropsChanged(true)
    }


    internal fun onAfterUpdateTransaction() {
        if (main.getPropsChanged()) {
            requestAd()
        }
        main.setPropsChanged(false)
    }

    internal fun onDropViewInstance() {
        val adView = getAdView()
        if (adView != null) {
            adView.setAdListener(null)
            adView.onDestroy()
            main.removeView(adView)
        }
    }

    internal fun commandReady() {
        val adView = getAdView()
        if (adView != null) {
            onReady(mapOf(
                "ready" to adView.isReady
            ))
        }
    }

    internal fun commandShowAd() {
        val adView = getAdView()
        adView?.showAd()
    }

    private fun requestAd() {
        val placementId = main.getPlacementId()
        val autoDestroy = main.getAutoDestroy()
        val closeAutoShow = main.getCloseAutoShow()

        val adView = initAdView()

        if (adView != null) {
            if (closeAutoShow) {
                adView.closeAutoShow()
            }
            adView.setAutoDestroy(autoDestroy)
            adView.loadAd(placementId)
        }
    }
}