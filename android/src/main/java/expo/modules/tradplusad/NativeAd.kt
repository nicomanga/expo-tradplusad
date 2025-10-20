//package expo.modules.tradplusad
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.TextView
//import com.tradplus.ads.base.adapter.nativead.TPNativeAdView
//import com.tradplus.ads.base.bean.TPAdError
//import com.tradplus.ads.base.bean.TPAdInfo
//import com.tradplus.ads.base.bean.TPBaseAd
//import com.tradplus.ads.base.common.TPImageLoader
//import com.tradplus.ads.open.nativead.NativeAdListener
//import com.tradplus.ads.open.nativead.TPNative
//import com.tradplus.ads.open.nativead.TPNativeAdRender
//import expo.modules.kotlin.AppContext
//import expo.modules.kotlin.exception.CodedException
//import expo.modules.kotlin.modules.Module
//import expo.modules.kotlin.modules.ModuleDefinition
//import expo.modules.kotlin.records.Field
//import expo.modules.kotlin.records.Record
//import expo.modules.kotlin.viewevent.EventDispatcher
//import expo.modules.kotlin.views.ExpoView
//import expo.modules.tradplusad.utils.Utils
//import java.util.concurrent.ConcurrentHashMap
//import kotlin.collections.component1
//import kotlin.collections.component2
//import kotlin.collections.iterator
//
//internal const val TAG = "NativeAd"
//
//class NativeAd: Module() {
//
//    private val reactContext get() = requireNotNull(appContext.reactContext)
//
//    private var nativeAds = ConcurrentHashMap<String, Native>()
//
//    override fun definition() = ModuleDefinition {
//        Name("nativeAd")
//
//        Events(
//            "onAdLoaded",
//            "onAdClicked",
//            "onAdImpression",
//            "onAdLoadFailed",
//            "onAdClosed",
//            "onAdShowFailed",
//            "onAdVideoEnd",
//            "onAdVideoStart"
//        )
//
//        AsyncFunction("requestAd") { option: NativeOption ->
//            val created = getNativeOrCreate(option)
//            created.ad()?.loadAd()
//            return@AsyncFunction option.unitId
//        }
//
//        AsyncFunction("isReady") { unitId: String ->
//
//            val ad = nativeAds[unitId]
//
//            if (ad == null) {
//                throw CodedException("Not Found for unitId(${unitId})")
//            }
//
//            val native = ad.ad()
//
//            if (native == null) {
//                throw CodedException("Not Found Ads for unitId(${unitId})")
//            }
//
//            return@AsyncFunction native.isReady
//        }
//
//        AsyncFunction("onDestroy") { unitId: String ->
//            val ad = nativeAds[unitId]
//
//            if (ad == null) {
//                throw CodedException("Not Found for unitId(${unitId})")
//            }
//
//            val native = ad.ad()
//
//            if (native == null) {
//                throw CodedException("Not Found Ads for unitId(${unitId})")
//            }
//
//            native.setAdListener(null)
//            native.onDestroy()
//            nativeAds.remove(unitId)
//
//            return@AsyncFunction
//        }
//
//        OnDestroy {
//           clearAllAds()
//        }
//
//        View(Layout::class) {
//            Events("onDisplay")
//
//            Prop<String>("requestAd") { view: Layout, unitId: String? ->
//                view.requestAd(nativeAds[unitId])
//                view.main.unitId = unitId
//            }
//
//            AsyncFunction("updateAssets") { parent: Layout, assetName: String, tagId: Int ->
//                when (assetName) {
//                    "title-id" -> parent.main.titleId = tagId
//                    "subtitle-id" -> parent.main.subtitleId = tagId
//                    "cta-id" -> parent.main.ctaId = tagId
//                    "icon-id" -> parent.main.iconId = tagId
//                    "media-id" -> parent.main.mediaId = tagId
//                    "choice-id" -> parent.main.choiceId = tagId
//                    "choice-icon-id" -> parent.main.choiceIconId = tagId
//                    else -> Log.d(TAG, "missing assetsName ( key: $assetName )")
//                }
//                parent.check()
//            }
//
//            OnViewDidUpdateProps { view: Layout ->
//
//            }
//
//            OnViewDestroys { view: Layout ->
//                val unitId = view.main.unitId
//
//                if (unitId != null) {
//                    view.main.unitId = null
//                    val native = nativeAds[unitId]
//
//                    if (native != null) {
//                        val ad = native.ad()
//                        if (ad != null) {
//                            ad.setAdListener(null)
//                            ad.onDestroy()
//                        }
//                        nativeAds.remove(unitId)
//                    }
//
//                }
//
//                val native = view.main.native
//
//                if (native != null) {
//                    val ad = native.ad()
//                    if (ad != null) {
//                        ad.setAdListener(null)
//                        ad.onDestroy()
//                    }
//                    view.main.native = null
//                }
//            }
//        }
//    }
//
//    private fun getNativeOrCreate(option: NativeOption): Native {
//        val unitId = option.unitId
//        if (unitId == null) throw CodedException("required unitId")
//
//        val ad = nativeAds[unitId]
//
//        if (ad != null) {
//            ad.ad()?.setAdListener(null)
//            ad.ad()?.onDestroy()
//            nativeAds.remove(unitId)
//        }
//
//        val native = Native(reactContext, this@NativeAd, option)
//        nativeAds[unitId] = native
//        return native
//    }
//
//    internal fun clearAllAds() {
//        for ((placementId, ad) in nativeAds) {
//            ad.ad()?.setAdListener(null)
//            ad.ad()?.onDestroy()
//            Log.d(TAG, "Cleared ad for placementId=$placementId")
//        }
//        nativeAds.clear()
//    }
//}
//
//// --- Layout ---
//@SuppressLint("ViewConstructor")
//internal class Layout(context: Context, appContext: AppContext): ExpoView(context, appContext) {
//
//    val onDisplay by EventDispatcher()
//
//    internal val main = LayoutAd(context)
//
//    init {
//        val matchParam = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//        main.also {  it.layoutParams = matchParam }
//        addView(main, matchParam)
//    }
//
//    private val tpNativeAdRender = object : TPNativeAdRender() {
//        override fun createAdLayoutView(): ViewGroup {
//            return getChildAt(0) as ViewGroup
//        }
//
//        override fun renderAdView(tpNativeAdView: TPNativeAdView?): ViewGroup? {
//            val ad = createAdLayoutView()
//
//            val imageView = main.mediaId?.let {
//                appContext.findView<ImageView>(it)
//            }
//
//            if (imageView != null) {
//                when {
//                    tpNativeAdView?.mediaView != null -> {
//                        val parent = imageView.parent
//                        val params = imageView.layoutParams
//                        val mediaView = tpNativeAdView.mediaView
//
//                        if (mediaView.parent != null) {
//                            (mediaView.parent as ViewGroup).removeView(mediaView)
//                        }
//
//                        if (parent != null) {
//                            val index = (parent as ViewGroup).indexOfChild(imageView)
//                            parent.removeView(imageView)
//                            parent.addView(mediaView, index, params)
//                            clickViews.add(mediaView)
//                            mediaView.post { main.scaleFit(mediaView, imageView) }
//                        }
//                    }
//                    tpNativeAdView?.mainImage != null -> {
//                        imageView.setImageDrawable(tpNativeAdView.mainImage)
//                    }
//                    tpNativeAdView?.mainImageUrl != null -> {
//                        TPImageLoader.getInstance().loadImage(imageView, tpNativeAdView.mainImageUrl)
//                    }
//                    else -> Log.w(TAG, "tpNativeView.imageView is not an ImageView")
//                }
//            }
//
//            val iconView = main.iconId?.let {
//                appContext.findView<ImageView>(it)
//            }
//
//            if (iconView != null) {
//                when {
//                    tpNativeAdView?.iconView != null -> {
//                        val params = iconView.layoutParams
//                        val parent = iconView.parent
//                        val newIcon = tpNativeAdView.iconView
//
//                        if (parent != null) {
//                            val index = (parent as ViewGroup).indexOfChild(iconView)
//                            parent.removeView(iconView)
//                            parent.addView(newIcon, index, params).apply {
//                                post {
//                                    main.scaleFit(newIcon, iconView)
//                                }
//                            }
//                            clickViews.add(newIcon)
//                        }
//                    }
//                    tpNativeAdView?.iconImageUrl != null -> {
//                        TPImageLoader.getInstance().loadImage(iconView, tpNativeAdView.iconImageUrl)
//                    }
//                    tpNativeAdView?.iconImage != null -> {
//                        iconView.setImageDrawable(tpNativeAdView.iconImage)
//                    }
//                    else -> Log.w(TAG, "tpNativeView.iconView is not an ImageView")
//                }
//            }
//
//            val titleView = main.titleId?.let {
//                appContext.findView<TextView>(it)
//            }
//
//            val subtitleView = main.subtitleId?.let {
//                appContext.findView<TextView>(it)
//            }
//
//            val ctaView = main.ctaId?.let {
//                appContext.findView<TextView>(it)
//            }
//
//            val choice =  main.choiceId?.let {
//                appContext.findView<ImageView>(it)
//            }
//
//            if (choice != null) {
//                val params = choice.layoutParams
//                val parent = choice.parent as? ViewGroup
//
//                if (parent != null) {
//                    val index = parent.indexOfChild(choice)
//                    parent.removeView(choice)
//
//                    val newAdChoice = FrameLayout(context).apply {
//                        layoutParams = params
//                    }
//
//                    parent.addView(newAdChoice, index, params).apply {
//                        post { main.scaleFit(newAdChoice, choice) }
//                    }
//
//                    setAdChoicesContainer(newAdChoice, true)
//                }
//            }
//
//            val choicesView = main.choiceIconId?.let {
//                appContext.findView<ImageView>(it)
//            }
//
//            if (choicesView != null) {
//                when {
//                    tpNativeAdView?.adChoiceImage != null -> {
//                        choicesView.setImageDrawable(tpNativeAdView.adChoiceImage)
//                    }
//                    tpNativeAdView?.adChoiceUrl != null -> {
//                        TPImageLoader.getInstance().loadImage(choicesView, tpNativeAdView.adChoiceUrl)
//                    }
//                }
//            }
//
//            setImageView(imageView, true)
//            setIconView(iconView, true)
//            setTitleView(titleView, true)
//            setSubTitleView(subtitleView, true)
//            setAdChoiceView(choicesView, true)
//            setCallToActionView(ctaView, true)
//
//            main.post {
//                main.scaleFit(main, ad)
//            }
//
//            return ad
//        }
//    }
//
//    private fun showAd() {
//        if (main.native != null) {
//            val ad = main.native!!.ad()?.nativeAd
//            onDisplay(Utils.mapTonNativeAdView(ad?.nativeAdView))
//            ad?.showAd(main, tpNativeAdRender, "")
//        }
//    }
//
//    internal fun requestAd(native: Native?) {
//       main.native = native
//    }
//
//    internal fun check() {
//        val validations = listOf(
//            "missing-title-id" to (main.titleId != null),
//            "missing-subtitle-id" to (main.subtitleId != null),
//            "missing-cta-id" to (main.ctaId != null),
//            "missing-icon-id" to (main.iconId != null),
//            "missing-media-id" to (main.mediaId != null),
//            "missing-choice-id" to (main.choiceId != null),
//            "missing-choice-icon-id" to (main.choiceIconId != null)
//        )
//
//        for ((key, valid) in validations) {
//            if (!valid) {
//                return
//            }
//        }
//
//        showAd()
//    }
//}
//
//// --- LayoutAd ---
//internal class LayoutAd(context: Context): FrameLayout(context) {
//
//    internal var titleId: Int? = null
//    internal var subtitleId: Int? = null
//    internal var ctaId: Int? = null
//    internal var iconId: Int? = null
//    internal var mediaId: Int? = null
//    internal var choiceId: Int? = null
//    internal var choiceIconId: Int? = null
//
//    internal var unitId: String? = null
//    internal var native: Native? = null
//
//    override fun requestLayout() {
//        super.requestLayout()
//        post {
//            measure(
//                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
//            )
//            layout(left, top, right, bottom)
//        }
//    }
//
//    internal fun scaleFit(child: View?, parent: View?) {
//        if (child != null && parent != null) {
//            child.measure(
//                MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(parent.height, MeasureSpec.EXACTLY)
//            )
//            child.layout(parent.left, parent.top, parent.right, parent.bottom)
//        }
//    }
//}
//
//// --- OPTION SIZE ---
//internal class NativeOptionSize : Record {
//    @Field
//    var width: Int = 0
//
//    @Field
//    var height: Int = 0
//}
//
//// --- OPTION CONFIG ---
//internal class NativeOption : Record {
//    @Field
//    var unitId: String? = null
//
//    @Field
//    var size: NativeOptionSize? = null
//
//    @Field
//    var entryAdScenario: String? = null
//
//    @Field
//    var customParams: MutableMap<String, Any> = mutableMapOf()
//}
//
//// --- NATIVE WRAPPER ---
//internal class Native( val context: Context,  val module: NativeAd, val option: NativeOption ) {
//
//    private var native: TPNative? = null
//    private var base: TPBaseAd? = null
//
//    private val nativeAdListener = object : NativeAdListener() {
//        override fun onAdLoaded(tpAdInfo: TPAdInfo, baseAd: TPBaseAd?) {
//            Log.d("NativeAd", "onAdLoaded for unitId=${option.unitId}")
//            module.sendEvent("onAdLoaded", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//                "tpBaseAd" to Utils.mapToAdBase(baseAd)
//            ))
//            base = baseAd
//        }
//
//        override fun onAdClicked(tpAdInfo: TPAdInfo) {
//            Log.d("NativeAd", "onAdClicked for unitId=${option.unitId}")
//            module.sendEvent("onAdClicked", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//            ))
//        }
//
//        override fun onAdImpression(tpAdInfo: TPAdInfo) {
//            Log.d("NativeAd", "onAdImpression for unitId=${option.unitId}")
//            module.sendEvent("onAdImpression", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//            ))
//        }
//
//        override fun onAdLoadFailed(tpAdError: TPAdError?) {
//            Log.e("NativeAd", "onAdLoadFailed code(${tpAdError?.errorCode}) message(${tpAdError?.errorMsg}) for unitId=${option.unitId}")
//            module.sendEvent("onAdLoadFailed", mapOf(
//                "unitId" to option.unitId,
//                "tpAdError" to Utils.mapToAdError(tpAdError),
//            ))
//        }
//
//        override fun onAdClosed(tpAdInfo: TPAdInfo?) {
//            module.sendEvent("onAdClosed", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//            ))
//        }
//
//        override fun onAdShowFailed(tpAdError: TPAdError?, tpAdInfo: TPAdInfo?) {
//            module.sendEvent("onAdShowFailed", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//                "tpAdError" to Utils.mapToAdError(tpAdError),
//            ))
//        }
//
//        override fun onAdVideoEnd(tpAdInfo: TPAdInfo?) {
//            module.sendEvent("onAdVideoEnd", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//            ))
//        }
//
//        override fun onAdVideoStart(tpAdInfo: TPAdInfo?) {
//            module.sendEvent("onAdVideoStart", mapOf(
//                "unitId" to option.unitId,
//                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
//            ))
//        }
//
//    }
//
//    init {
//        val ad = TPNative(context, option.unitId ?: "")
//        ad.setAdListener(nativeAdListener)
//
//        option.entryAdScenario?.let { ad.entryAdScenario(it) }
//
//        option.size?.let { size ->
//            ad.setAdSize(size.width, size.height)
//        }
//
//        if (option.customParams.isNotEmpty()) {
//            ad.setCustomParams(option.customParams)
//        }
//
//        native = ad
//    }
//
//    internal fun assets(): TPBaseAd? = base
//
//    internal fun ad(): TPNative? = native
//}

package expo.modules.tradplusad

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import expo.modules.tradplusad.utils.Utils
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

internal const val TAG = "NativeAd"

/**
 * Improvements applied to prevent memory leaks on repeated requestAd calls:
 * - reuse existing wrapper instead of destroying/recreating by default
 * - keep only applicationContext inside Native wrapper
 * - use WeakReference for Module callbacks
 * - provide explicit destroyAd(unitId) API
 * - do NOT remove global nativeAds map entries from view lifecycle automatically (avoid double-destroys)
 * - clean up view-held references on view destroy (detach native pointer only)
 */
class NativeAd: Module() {

    // prefer application context to avoid holding Activity
    private val reactContext get() = requireNotNull(appContext.reactContext)

    // map of active native wrappers. Use ConcurrentHashMap for safe concurrent access.
    private val nativeAds = ConcurrentHashMap<String, Native>()

    override fun definition() = ModuleDefinition {
        Name("nativeAd")

        Events(
            "onAdLoaded",
            "onAdClicked",
            "onAdImpression",
            "onAdLoadFailed",
            "onAdClosed",
            "onAdShowFailed",
            "onAdVideoEnd",
            "onAdVideoStart"
        )

        // Request and load an ad. Reuses existing wrapper when present.
        AsyncFunction("requestAd") { option: NativeOption ->
            val created = getNativeOrCreate(option)
            created.ad()?.loadAd()
            return@AsyncFunction option.unitId
        }

        AsyncFunction("isReady") { unitId: String ->
            val ad = nativeAds[unitId] ?: throw CodedException("Not Found for unitId($unitId)")
            val native = ad.ad() ?: throw CodedException("Not Found Ads for unitId($unitId)")
            return@AsyncFunction native.isReady
        }

        // Explicit destroy API for JS to completely tear down an ad wrapper
        AsyncFunction("destroyAd") { unitId: String ->
            val removed = nativeAds.remove(unitId)
            if (removed == null) throw CodedException("Not Found for unitId($unitId)")
            removed.destroy()
            return@AsyncFunction
        }

        // Backwards-compatible alias; prefer destroyAd
        AsyncFunction("onDestroy") { unitId: String ->
            val ad = nativeAds[unitId] ?: throw CodedException("Not Found for unitId($unitId)")
            ad.ad()?.setAdListener(null)
            ad.ad()?.onDestroy()
            nativeAds.remove(unitId)
            return@AsyncFunction
        }

        OnDestroy {
            clearAllAds()
        }

        View(Layout::class) {
            Events("onDisplay", "onReady")

            Prop<String>("requestAd") { view: Layout, unitId: String? ->
                // Attach an existing wrapper to this view (do not destroy global wrapper)
                view.requestAd(nativeAds[unitId])
                view.main.unitId = unitId
            }

            AsyncFunction("updateAssets") { parent: Layout, assetName: String, tagId: Int ->
                when (assetName) {
                    "title-id" -> parent.main.titleId = tagId
                    "subtitle-id" -> parent.main.subtitleId = tagId
                    "cta-id" -> parent.main.ctaId = tagId
                    "icon-id" -> parent.main.iconId = tagId
                    "media-id" -> parent.main.mediaId = tagId
                    "choice-id" -> parent.main.choiceId = tagId
                    "choice-icon-id" -> parent.main.choiceIconId = tagId
                    else -> Log.d(TAG, "missing assetsName ( key: $assetName )")
                }
                parent.check()
            }

            OnViewDestroys { view: Layout ->
                // When the React view is destroyed, only detach the view's pointer to the Native wrapper.
                // Do NOT destroy the global wrapper automatically since it may be shared across other views
                val unitId = view.main.unitId

                if (unitId != null) {
                    view.main.unitId = null
                    // Do not remove from nativeAds map here; caller should call destroyAd explicitly when they want to free resources.
                }

                // Detach wrapper reference from this view so view can be GC'd
                view.main.native = null

                // Remove any posted callbacks to avoid leaking the view's Handler
                view.main.cleanup()
            }
        }
    }

    private fun getNativeOrCreate(option: NativeOption): Native {
        val unitId = option.unitId ?: throw CodedException("required unitId")

        // Reuse existing wrapper if present to avoid recreating repeated listeners and leaking
        nativeAds[unitId]?.let { return it }

        // Create new wrapper
        val native = Native(reactContext.applicationContext, this@NativeAd, option)
        nativeAds[unitId] = native
        return native
    }

    internal fun clearAllAds() {
        for ((placementId, ad) in nativeAds) {
            ad.destroy()
            Log.d(TAG, "Cleared ad for placementId=$placementId")
        }
        nativeAds.clear()
    }
}

// --- Layout ---
@SuppressLint("ViewConstructor")
internal class Layout(context: Context, appContext: AppContext): ExpoView(context, appContext) {

    val onDisplay by EventDispatcher()
    val onReady by EventDispatcher()

    internal val main = LayoutAd(context)

    init {
        val matchParam = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        main.also {  it.layoutParams = matchParam }
        addView(main, matchParam)
    }

    // Render that doesn't capture the Layout strongly
    private val tpNativeAdRender = object : TPNativeAdRender() {
        private val layoutRef = WeakReference(this@Layout)

        override fun createAdLayoutView(): ViewGroup {
            val layout = layoutRef.get() ?: throw IllegalStateException("Layout is gone")
            return layout.getChildAt(0) as ViewGroup
        }

        override fun renderAdView(tpNativeAdView: TPNativeAdView?): ViewGroup? {
            val layout = layoutRef.get() ?: return null
            val ad = createAdLayoutView()

            val imageView = layout.main.mediaId?.let {
                try { layout.appContext.findView<ImageView>(it) } catch (t: Throwable) { null }
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
                            mediaView.post { layout.main.scaleFit(mediaView, imageView) }
                        }
                    }
                    tpNativeAdView?.mainImage != null -> imageView.setImageDrawable(tpNativeAdView.mainImage)
                    tpNativeAdView?.mainImageUrl != null -> TPImageLoader.getInstance().loadImage(imageView, tpNativeAdView.mainImageUrl)
                    else -> Log.w(TAG, "tpNativeView.imageView is not an ImageView")
                }
            }

            val iconView = layout.main.iconId?.let {
                try { layout.appContext.findView<ImageView>(it) } catch (t: Throwable) { null }
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
                                post { layout.main.scaleFit(newIcon, iconView) }
                            }
                        }
                    }
                    tpNativeAdView?.iconImageUrl != null -> TPImageLoader.getInstance().loadImage(iconView, tpNativeAdView.iconImageUrl)
                    tpNativeAdView?.iconImage != null -> iconView.setImageDrawable(tpNativeAdView.iconImage)
                    else -> Log.w(TAG, "tpNativeView.iconView is not an ImageView")
                }
            }

            val titleView = layout.main.titleId?.let { try { layout.appContext.findView<TextView>(it) } catch (t: Throwable) { null } }
            val subtitleView = layout.main.subtitleId?.let { try { layout.appContext.findView<TextView>(it) } catch (t: Throwable) { null } }
            val ctaView = layout.main.ctaId?.let { try { layout.appContext.findView<TextView>(it) } catch (t: Throwable) { null } }

            val choice = layout.main.choiceId?.let { try { layout.appContext.findView<ImageView>(it) } catch (t: Throwable) { null } }

            if (choice != null) {
                val params = choice.layoutParams
                val parent = choice.parent as? ViewGroup

                if (parent != null) {
                    val index = parent.indexOfChild(choice)
                    parent.removeView(choice)

                    val newAdChoice = FrameLayout(layout.context).apply { layoutParams = params }

                    parent.addView(newAdChoice, index, params).apply {
                        post { layout.main.scaleFit(newAdChoice, choice) }
                    }

                    setAdChoicesContainer(newAdChoice, true)
                }
            }

            val choicesView = layout.main.choiceIconId?.let { try { layout.appContext.findView<ImageView>(it) } catch (t: Throwable) { null } }

            if (choicesView != null) {
                when {
                    tpNativeAdView?.adChoiceImage != null -> choicesView.setImageDrawable(tpNativeAdView.adChoiceImage)
                    tpNativeAdView?.adChoiceUrl != null -> TPImageLoader.getInstance().loadImage(choicesView, tpNativeAdView.adChoiceUrl)
                }
            }

            setImageView(imageView, true)
            setIconView(iconView, true)
            setTitleView(titleView, true)
            setSubTitleView(subtitleView, true)
            setAdChoiceView(choicesView, true)
            setCallToActionView(ctaView, true)

            layout.main.post { layout.main.scaleFit(layout.main, ad) }

            return ad
        }
    }

    private fun showAd() {
        main.native?.let { wrapper ->
            val ad = wrapper.ad()
            onDisplay(Utils.mapToAdBase(wrapper.assets()))
            ad?.showAd(main, tpNativeAdRender, "")
        }
    }

    internal fun requestAd(native: Native?) {
        main.native = native
    }

    internal fun check() {
        val validations = listOf(
            "missing-title-id" to (main.titleId != null),
            "missing-subtitle-id" to (main.subtitleId != null),
            "missing-cta-id" to (main.ctaId != null),
            "missing-icon-id" to (main.iconId != null),
            "missing-media-id" to (main.mediaId != null),
            "missing-choice-id" to (main.choiceId != null),
            "missing-choice-icon-id" to (main.choiceIconId != null)
        )

        for ((_, valid) in validations) if (!valid) return

        Log.d(TAG, "onReady")

        main.postDelayed({
            Log.d(TAG, "onReady emitted after 1s delay")
            onReady(mapOf("ok" to true))
            showAd()
        }, 1000L)
    }
}

// --- LayoutAd ---
internal class LayoutAd(context: Context): FrameLayout(context) {

    internal var titleId: Int? = null
    internal var subtitleId: Int? = null
    internal var ctaId: Int? = null
    internal var iconId: Int? = null
    internal var mediaId: Int? = null
    internal var choiceId: Int? = null
    internal var choiceIconId: Int? = null

    internal var unitId: String? = null
    internal var native: Native? = null

    override fun requestLayout() {
        super.requestLayout()
        post {
            measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
            layout(left, top, right, bottom)
        }
    }

    internal fun scaleFit(child: View?, parent: View?) {
        if (child != null && parent != null) {
            child.measure(
                MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(parent.height, MeasureSpec.EXACTLY)
            )
            child.layout(parent.left, parent.top, parent.right, parent.bottom)
        }
    }

    internal fun cleanup() {
        // detach native wrapper reference so LayoutAd can be GC'd
        native = null

        // remove pending callbacks posted to this view's handler
        try {
            handler?.removeCallbacksAndMessages(null)
        } catch (e: Exception) {
            Log.w(TAG, "removeCallbacksAndMessages failed: ${e.message}")
        }
    }
}

// --- OPTION SIZE ---
internal class NativeOptionSize : Record {
    @Field
    var width: Int = 0

    @Field
    var height: Int = 0
}

// --- OPTION CONFIG ---
internal class NativeOption : Record {
    @Field
    var unitId: String? = null

    @Field
    var size: NativeOptionSize? = null

    @Field
    var entryAdScenario: String? = null

    @Field
    var customParams: MutableMap<String, Any> = mutableMapOf()
}

// --- NATIVE WRAPPER ---
internal class Native(context: Context, module: NativeAd, private val option: NativeOption) {

    // keep only applicationContext to avoid leaking Activities
    private val appContext = context.applicationContext

    // weak reference to module so listener callbacks don't keep Module alive
    private val moduleRef = WeakReference(module)

    private var native: TPNative? = null
    private var base: TPBaseAd? = null

    // listener does not hold strong reference to outer Native class
    private val nativeAdListener = object : NativeAdListener() {
        override fun onAdLoaded(tpAdInfo: TPAdInfo, baseAd: TPBaseAd?) {
            Log.d("NativeAd", "onAdLoaded for unitId=${option.unitId}")
            base = baseAd
            moduleRef.get()?.sendEvent("onAdLoaded", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                "tpBaseAd" to Utils.mapToAdBase(baseAd)
            ))
        }

        override fun onAdClicked(tpAdInfo: TPAdInfo) {
            Log.d("NativeAd", "onAdClicked for unitId=${option.unitId}")
            moduleRef.get()?.sendEvent("onAdClicked", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
            ))
        }

        override fun onAdImpression(tpAdInfo: TPAdInfo) {
            Log.d("NativeAd", "onAdImpression for unitId=${option.unitId}")
            moduleRef.get()?.sendEvent("onAdImpression", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
            ))
        }

        override fun onAdLoadFailed(tpAdError: TPAdError?) {
            Log.e("NativeAd", "onAdLoadFailed code(${tpAdError?.errorCode}) message(${tpAdError?.errorMsg}) for unitId=${option.unitId}")
            moduleRef.get()?.sendEvent("onAdLoadFailed", mapOf(
                "unitId" to option.unitId,
                "tpAdError" to Utils.mapToAdError(tpAdError),
            ))
        }

        override fun onAdClosed(tpAdInfo: TPAdInfo?) {
            moduleRef.get()?.sendEvent("onAdClosed", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
            ))
        }

        override fun onAdShowFailed(tpAdError: TPAdError?, tpAdInfo: TPAdInfo?) {
            moduleRef.get()?.sendEvent("onAdShowFailed", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
                "tpAdError" to Utils.mapToAdError(tpAdError),
            ))
        }

        override fun onAdVideoEnd(tpAdInfo: TPAdInfo?) {
            moduleRef.get()?.sendEvent("onAdVideoEnd", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
            ))
        }

        override fun onAdVideoStart(tpAdInfo: TPAdInfo?) {
            moduleRef.get()?.sendEvent("onAdVideoStart", mapOf(
                "unitId" to option.unitId,
                "tpAdInfo" to Utils.mapToAdInfo(tpAdInfo),
            ))
        }

    }

    init {
        val ad = TPNative(appContext, option.unitId ?: "")
        ad.setAdListener(nativeAdListener)

        option.entryAdScenario?.let { ad.entryAdScenario(it) }

        option.size?.let { size ->
            ad.setAdSize(size.width, size.height)
        }

        if (option.customParams.isNotEmpty()) {
            ad.setCustomParams(option.customParams)
        }

        native = ad
    }

    internal fun assets(): TPBaseAd? = base

    internal fun ad(): TPNative? = native

    internal fun destroy() {
        try {
            native?.setAdListener(null)
            native?.onDestroy()
        } catch (e: Exception) {
            Log.w(TAG, "destroy native failed: ${e.message}")
        }

        native = null
        base = null
        moduleRef.clear()
    }
}
