package expo.modules.tradplusad

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.tradplus.ads.open.nativead.TPNative
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.tradplusad.common.ReactNativeNativeAdView
import expo.modules.tradplusad.utils.ReactNativeModule
import expo.modules.tradplusad.views.NativeView

class ExpoNativeAdModule: Module() {

    companion object {
        const val TAG = "ExpoNativeAd"
    }

    override fun definition() =  ModuleDefinition {
        Name(TAG)

        View(NativeView::class) {

            Events(
                "onAdLoaded",
                "onAdClicked",
                "onAdClosed",
                "onAdImpression",
                "onAdShowFailed",
                "onAdVideoEnd",
                "onAdVideoStart",
                "onAdLoadFailed",
                "onValidator"
            )

            Prop<ReactNativeModule.Native?>("request") { parent: NativeView, request ->
                parent.main.request = request
                parent.main.propsChanged = true
            }

            AsyncFunction("updateAssets") { parent: NativeView, assetName: String, tagId: Int ->
                when (assetName) {
                    "title-id" -> {
                        parent.main.titleID = tagId
                    }
                    "subtitle-id" -> {
                        parent.main.subtitleID = tagId
                    }
                    "cta-id" -> {
                        parent.main.ctaID = tagId
                    }
                    "icon-id" -> {
                        parent.main.iconID = tagId
                    }
                    "media-id" -> {
                        parent.main.mediaID = tagId
                    }
                    "choice-id" -> {
                        parent.main.choiceID = tagId
                    }
                    "choice-icon-id" -> {
                        parent.main.choiceIconID = tagId
                    }
                    else -> Log.d(TAG, "missing assetsName  ( key: $assetName )")
                }
            }

            AsyncFunction("requestAd") { parent: NativeView ->
                parent.requestAd()
            }

            AsyncFunction("check") { parent: NativeView ->
                parent.check()
            }

            AsyncFunction("showAd") { parent: NativeView ->
                parent.showAd()
            }

            OnViewDidUpdateProps { parent: NativeView ->
               parent.onUpdateProps()
            }

            GroupView<NativeView> {
                AddChildView { parent, child: View, index ->
                    parent.addView(child, index)
                }
            }
        }
    }
}