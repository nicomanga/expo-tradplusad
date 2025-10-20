package expo.modules.tradplusad

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.tradplusad.views.BannerView

class ExpoBannerAdViewModule: Module() {
    override fun definition() = ModuleDefinition {
        Name("ExpoBannerAd")

        View(BannerView::class) {
            Events(
                "onReady",
                "onAdLoaded",
                "onAdClicked",
                "onAdClosed",
                "onAdImpression",
                "onAdLoadFailed",
                "onAdShowFailed",
                "onBannerRefreshed"
            )

            Prop("placementId") { ad: BannerView, placementId: String ->
                ad.setPlacementId(placementId)
            }

            Prop("entryAdScenario") { ad: BannerView, entryAdScenario: String ->
                ad.setEntryAdScenario(entryAdScenario)
            }

            Prop("autoDestroy") { ad: BannerView, autoDestroy: Boolean ->
                ad.setAutoDestroy(autoDestroy)
            }

            Prop("closeAutoShow") { ad: BannerView, closeAutoShow: Boolean ->
                ad.setCloseAutoShow(closeAutoShow)
            }

            AsyncFunction("ready") { it: BannerView ->
                it.commandReady()
            }

            AsyncFunction("showAd") { it: BannerView ->
                it.commandShowAd()
            }

            OnViewDidUpdateProps { it ->
                it.onAfterUpdateTransaction()
            }

            OnViewDestroys { it ->
                it.onDropViewInstance()
            }
        }
    }
}