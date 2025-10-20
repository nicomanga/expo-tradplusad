package expo.modules.tradplusad.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.tradplus.ads.base.adapter.nativead.TPNativeAdView
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.base.bean.TPBaseAd
import com.tradplus.ads.base.common.TPImageLoader
import com.tradplus.ads.open.nativead.NativeAdListener
import com.tradplus.ads.open.nativead.TPNative
import com.tradplus.ads.open.nativead.TPNativeAdRender
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.exception.CodedException
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import expo.modules.tradplusad.common.ReactNativeNativeAdView
import expo.modules.tradplusad.utils.Utils
import androidx.core.view.isNotEmpty


@SuppressLint("ViewConstructor")
class NativeView(context: Context, appContext: AppContext): ExpoView(context, appContext) {

    val onAdLoaded by EventDispatcher()
    val onAdClicked by EventDispatcher()
    val onAdClosed by EventDispatcher()
    val onAdImpression by EventDispatcher()
    val onAdShowFailed by EventDispatcher()
    val onAdVideoEnd by EventDispatcher()
    val onAdVideoStart by EventDispatcher()
    val onAdLoadFailed by EventDispatcher()
    val onValidator by EventDispatcher()

    internal val main = ReactNativeNativeAdView(context).also {
        it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    init {
        addView(main)
    }

    private fun getAdNative(): TPNative {
        val placementId = main.request?.unitId
            ?: throw CodedException("error-unit-id", "missing [unit]", null)

        val entryAdScenario = main.request?.entryAdScenario
        val size = main.request?.size
        val customParams = main.request?.customParams

        return main.baseAd ?: TPNative(appContext.reactContext, placementId).also { tpNative ->

            if (entryAdScenario != null) {
                tpNative.entryAdScenario(entryAdScenario)
            }

            tpNative.setAdListener(object : NativeAdListener() {

                override fun onAdLoaded(tpAdInfo: TPAdInfo, baseAd: TPBaseAd) {
                    Log.d(TAG, "onAdLoaded")
                    main.showAd = true
                    onAdLoaded(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                        "tpBaseAd" to Utils.mapToAdBase(baseAd)
                    ))
                }

                override fun onAdClicked(tpAdInfo: TPAdInfo?) {
                    onAdClicked(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                    ))
                }

                override fun onAdClosed(tpAdInfo: TPAdInfo?) {
                    onAdClosed(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                    ))
                }

                override fun onAdImpression(tpAdInfo: TPAdInfo?) {
                    onAdImpression(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                    ))
                }

                override fun onAdShowFailed(tpAdError: TPAdError?, tpAdInfo: TPAdInfo?) {
                    onAdShowFailed(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                        "tpAdError" to Utils.mapToAdError(tpAdError)
                    ))
                }

                override fun onAdVideoEnd(tpAdInfo: TPAdInfo?) {
                    onAdVideoEnd(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                    ))
                }

                override fun onAdVideoStart(tpAdInfo: TPAdInfo?) {
                    onAdVideoStart(mapOf(
                        "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                    ))
                }


                override fun onAdLoadFailed(tpAdError: TPAdError) {
                    onAdLoadFailed(mapOf(
                        "tpAdError" to Utils.mapToAdError(tpAdError)
                    ))
                }
            })

            if (size != null) {
                tpNative.setAdSize(size.width, size.height)
            }

            if (customParams != null) {
                tpNative.setCustomParams(Utils.mapCustomParamsNotDouble(customParams))
            }

            main.baseAd = tpNative
        }
    }

    private fun getLayout(): ViewGroup? {
        val child = getChildAt(0)
        return if (child != null && child.javaClass.name == "com.facebook.react.views.view.ReactViewGroup") {
            child as ViewGroup
        } else {
            null
        }
    }

    private fun renderAd() {
        if (!main.showAd) return

        val ad = getAdNative()
        val custom = ad.nativeAd ?: return

        main.showAd = false
        custom.showAd(main, object : TPNativeAdRender() {
            override fun createAdLayoutView(): ViewGroup? {
                return getLayout()
            }

            override fun renderAdView(tpNativeAdView: TPNativeAdView?): ViewGroup? {
                val adLayout = createAdLayoutView()

                val imageView = main.mediaID?.let {
                    appContext.findView<ImageView>(it)
                }

                if (imageView != null) {
                    when {
                        tpNativeAdView?.mediaView != null -> {
                            val parent = imageView.parent
                            val params = imageView.layoutParams
                            val mediaView = tpNativeAdView.mediaView

                            if (mediaView.parent != null) {
                                (mediaView.parent as ViewGroup).removeView(mediaView)
                            }

                            if (parent != null) {
                                val index = (parent as ViewGroup).indexOfChild(imageView)
                                parent.removeView(imageView)
                                parent.addView(mediaView, index, params)
                                clickViews.add(mediaView)
                                mediaView.post { main.scaleFit(mediaView, imageView) }
                            }
                        }
                        tpNativeAdView?.mainImage != null -> {
                            imageView.setImageDrawable(tpNativeAdView.mainImage)
                        }
                        tpNativeAdView?.mainImageUrl != null -> {
                            TPImageLoader.getInstance().loadImage(imageView, tpNativeAdView.mainImageUrl)
                        }
                        else -> Log.w(TAG, "tpNativeView.imageView is not an ImageView")
                    }
                }

                val iconView = main.iconID?.let {
                    appContext.findView<ImageView>(it)
                }

                if (iconView != null) {
                    when {
                        tpNativeAdView?.iconView != null -> {
                            val params = iconView.layoutParams
                            val parent = iconView.parent
                            val newIcon = tpNativeAdView.iconView

                            if (parent != null) {
                                val index = (parent as ViewGroup).indexOfChild(iconView)
                                parent.removeView(iconView)
                                parent.addView(newIcon, index, params).apply {
                                    post {
                                        main.scaleFit(newIcon, iconView)
                                    }
                                }
                                clickViews.add(newIcon)
                            }
                        }
                        tpNativeAdView?.iconImageUrl != null -> {
                            TPImageLoader.getInstance().loadImage(iconView, tpNativeAdView.iconImageUrl)
                        }
                        tpNativeAdView?.iconImage != null -> {
                            iconView.setImageDrawable(tpNativeAdView.iconImage)
                        }
                        else -> Log.w(TAG, "tpNativeView.iconView is not an ImageView")
                    }
                }

                val titleView = main.titleID?.let {
                    appContext.findView<TextView>(it)
                }

                val subtitleView = main.subtitleID?.let {
                    appContext.findView<TextView>(it)
                }

                val ctaView = main.ctaID?.let {
                    appContext.findView<TextView>(it)
                }

                val choice =  main.choiceID?.let {
                    appContext.findView<ImageView>(it)
                }

                if (choice != null) {
                    val params = choice.layoutParams
                    val parent = choice.parent as? ViewGroup

                    if (parent != null) {
                        val index = parent.indexOfChild(choice)
                        parent.removeView(choice)

                        val newAdChoice = FrameLayout(context).apply {
                            layoutParams = params
                        }

                        parent.addView(newAdChoice, index, params).apply {
                            post { main.scaleFit(newAdChoice, choice) }
                        }

                        setAdChoicesContainer(newAdChoice, true)
                    }
                }

                val choicesView = main.choiceIconID?.let {
                    appContext.findView<ImageView>(it)
                }

                if (choicesView != null) {
                    when {
                        tpNativeAdView?.adChoiceImage != null -> {
                            choicesView.setImageDrawable(tpNativeAdView.adChoiceImage)
                        }
                        tpNativeAdView?.adChoiceUrl != null -> {
                            TPImageLoader.getInstance().loadImage(choicesView, tpNativeAdView.adChoiceUrl)
                        }
                    }
                }

                setImageView(imageView, true)
                setIconView(iconView, true)
                setTitleView(titleView, true)
                setSubTitleView(subtitleView, true)
                setAdChoiceView(choicesView, true)
                setCallToActionView(ctaView, true)

//                setAdChoicesContainer(FrameLayout(context).apply {
//                    layoutParams = LayoutParams(
//                        LayoutParams.WRAP_CONTENT,
//                        LayoutParams.WRAP_CONTENT
//                    )
//                }, true)

                main.post {
                    main.scaleFit(main, adLayout)
                }

                return adLayout
            }
        }, "")
    }

    internal fun requestAd() {
        Log.d(TAG, "requestAd")

        val child = getChildAt(0)

        when {
            child != null && child.javaClass.name == "com.facebook.react.views.view.ReactViewGroup" -> {
                Log.d(TAG, "Detected ReactViewGroup, caching it")
                main.cacheView = child as ViewGroup
            }
            child == main -> {
                Log.d(TAG, "Child is main, restoring cached ReactViewGroup if available")

                if (main.isNotEmpty()) {
                    main.removeAllViews()
                    onDestroyed()
                }

                main.cacheView?.let {
                    if (it.parent != null) {
                        (it.parent as? ViewGroup)?.removeView(it)
                    }
                    addView(it, 0)
                    Log.d(TAG, "Cached ReactViewGroup re-added to layout")
                } ?: run {
                    Log.w(TAG, "No cached ReactViewGroup found to re-add")
                }
            }
            else -> {
                Log.w(TAG, "No ReactViewGroup found, trying to reattach cached view")
                main.cacheView?.let {
                    if (it.parent != null) {
                        (it.parent as? ViewGroup)?.removeView(it)
                    }
                    addView(it, 0)
                    Log.d(TAG, "Cached view added successfully")
                } ?: run {
                    Log.w(TAG, "Cache view is null, nothing to add")
                }
            }
        }

        val ad = getAdNative()
        ad.loadAd()
    }

    internal fun onDestroyed() {
        val ad = getAdNative()
        ad.onDestroy()
        main.baseAd = null
    }

    internal fun showAd() {

        if (!main.validator) {
            return
        }

        val ad = getAdNative()
        if (ad.isReady) {
            renderAd()
        }
    }

//    internal fun check() {
//
//        if (main.propsChanged) {
//            Log.d(TAG, "validator")
//            onValidator(Utils.mapValidator("missing-props", false))
//            return
//        }
//
//        if (main.titleID == null) {
//            onValidator(Utils.mapValidator("missing-title-id", false))
//            return
//        }
//
//        if (main.subtitleID == null) {
//            onValidator(Utils.mapValidator("missing-subtitle-id", false))
//            return
//        }
//
//        if (main.ctaID == null) {
//            onValidator(Utils.mapValidator("missing-cta-id", false))
//            return
//        }
//
//        if (main.iconID == null) {
//            onValidator(Utils.mapValidator("missing-icon-id", false))
//            return
//        }
//
//        if (main.mediaID == null) {
//            onValidator(Utils.mapValidator("missing-media-id", false))
//            return
//        }
//
//        if (main.choiceID == null) {
//            onValidator(Utils.mapValidator("missing-choice-id", false))
//            return
//        }
//
//        if (main.choiceIconID == null) {
//            onValidator(Utils.mapValidator("missing-choice-icon-id", false))
//            return
//        }
//
//        onValidator(Utils.mapValidator("completed", true))
//        main.validator = true
//        return
//    }

    internal fun check() {
        val validations = listOf(
            "missing-title-id" to (main.titleID != null),
            "missing-subtitle-id" to (main.subtitleID != null),
            "missing-cta-id" to (main.ctaID != null),
            "missing-icon-id" to (main.iconID != null),
            "missing-media-id" to (main.mediaID != null),
            "missing-choice-id" to (main.choiceID != null),
            "missing-choice-icon-id" to (main.choiceIconID != null)
        )

        if (main.propsChanged) {
            onValidator(Utils.mapValidator("missing-props", false))
            return
        }

        for ((key, valid) in validations) {
            if (!valid) {
                onValidator(Utils.mapValidator(key, false))
                return
            }
        }

        onValidator(Utils.mapValidator("completed", true))
        main.validator = true
    }

    internal fun onUpdateProps() {
        if (main.propsChanged) {
            getAdNative()
        }
        main.propsChanged = false
    }

    companion object {
        private const val TAG = "ExpoNativeAd"
    }
}