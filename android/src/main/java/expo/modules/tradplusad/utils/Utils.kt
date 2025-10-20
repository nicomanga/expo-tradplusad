package expo.modules.tradplusad.utils

import com.tradplus.ads.base.adapter.nativead.TPNativeAdView
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.base.bean.TPBaseAd
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

object Utils {

    fun mapToAdInfo(tp: TPAdInfo?): Map<String, Any?> {
        if (tp == null) return emptyMap()
        return mapOf(
            "tpAdUnitId" to tp.tpAdUnitId,
            "trueAdUnitId" to tp.true_adunit_id,
            "adSourceName" to tp.adSourceName,
            "adNetworkId" to tp.adNetworkId,
            "adSourceId" to tp.adSourceId,
            "ecpm" to tp.ecpm,
            "ecpmCny" to tp.ecpmcny,
            "ecpmLevel" to tp.ecpmLevel,
            "ecpmPrecision" to tp.ecpmPrecision,
            "loadTime" to tp.loadTime,
            "rewardName" to tp.rewardName,
            "rewardNumber" to tp.rewardNumber,
            "isoCode" to tp.isoCode,
            "height" to tp.height,
            "width" to tp.width,
            "adViewHeight" to tp.adViewHeight,
            "adViewWidth" to tp.adViewWidth,
            "isBiddingNetwork" to tp.isBiddingNetwork,
            "waterfallIndex" to tp.waterfallIndex,
            "requestId" to tp.requestId,
            "impressionId" to tp.impressionId,
            "subChannel" to tp.subChannel,
            "channel" to tp.channel,
            "sceneId" to tp.sceneId,
            "configString" to tp.configString,
            "format" to tp.format,
            "videoProtocol" to tp.video_protocol,
            "bucketId" to tp.bucketId,
            "segmentId" to tp.segmentId,
            "adSourcePlacementId" to tp.adSourcePlacementId,
            "isExclusive" to tp.is_exclusive,
            "bannerW" to tp.bannerW,
            "bannerH" to tp.bannerH,
            "placementAdType" to tp.placementAdType,
            "isBottom" to tp.isBottom
        )
    }

    fun mapToAdError(tp: TPAdError?): Map<String, Any?> {
        if (tp == null) return emptyMap()
        return mapOf(
            "message" to tp.errorMsg,
            "code" to tp.errorCode
        )
    }

    fun mapToAdBase(tp: TPBaseAd?): Map<String, Any> {
        if (tp == null) return emptyMap()
        return mapTonNativeAdView(tp.tpNativeView)
    }

    fun mapTonNativeAdView(nativeAdView: TPNativeAdView?): Map<String, Any> {

        if (nativeAdView == null) return emptyMap()

        val aspectRatio = nativeAdView.aspectRatio
        val sponsoredLabel = nativeAdView.sponsoredLabel
        val huaweiAdInfoValue = nativeAdView.huaweiAdInfoValue
        val huaweiAdInfoKey = nativeAdView.huaweiAdInfoKey
        val huaweiAdInfoSeq = nativeAdView.huaweiAdInfoSeq
        val lastUpdateTime = nativeAdView.lastUpdateTime
        val nativeAdSocialContext = nativeAdView.nativeAdSocialContext

        val clickUrl = nativeAdView.clickUrl
        val callToAction = nativeAdView.callToAction
        val title = nativeAdView.title
        val subtitle = nativeAdView.subTitle
        val starRating = nativeAdView.starRating
        val videoUrl = nativeAdView.videoUrl
        val adChoiceUrl = nativeAdView.adChoiceUrl
        val adSource = nativeAdView.adSource
        val appName = nativeAdView.appName
        val authorName = nativeAdView.authorName
        val packageSizeBytes = nativeAdView.packageSizeBytes
        val permissionsUrl = nativeAdView.permissionsUrl
        val privacyAgreement = nativeAdView.privacyAgreement
        val versionName = nativeAdView.versionName
        val advertiserName = nativeAdView.advertiserName


        val picUrls = nativeAdView.picUrls

        val mainImageUrl = nativeAdView.mainImageUrl
        val iconImageUrl = nativeAdView.iconImageUrl
        val iconImage = nativeAdView.iconImage != null
        val adChoiceImage = nativeAdView.adChoiceImage != null
        val mainImage = nativeAdView.mainImage != null
        val iconView = nativeAdView.iconView != null
        val mediaView = nativeAdView.mediaView != null
        val adChoiceView = nativeAdView.adChoiceView != null


        val nativeAd = mapOf(
            "mainImageUrl" to (mainImageUrl != null),
            "iconImageUrl" to (iconImageUrl != null),
            "iconImage" to iconImage,
            "adChoiceImage" to adChoiceImage,
            "mainImage" to mainImage,
            "iconView" to iconView,
            "mediaView" to mediaView,
            "adChoiceView" to adChoiceView
        )

        return mapOf(
            "aspectRatio" to aspectRatio,
            "sponsoredLabel" to sponsoredLabel,
            "huaweiAdInfoValue" to huaweiAdInfoValue,
            "huaweiAdInfoKey" to huaweiAdInfoKey,
            "huaweiAdInfoSeq" to huaweiAdInfoSeq,
            "lastUpdateTime" to lastUpdateTime,
            "nativeAdSocialContext" to nativeAdSocialContext,
            "iconImageUrl" to iconImageUrl,
            "mainImageUrl" to mainImageUrl,
            "clickUrl" to clickUrl,
            "title" to title,
            "subtitle" to subtitle,
            "callToAction" to callToAction,
            "starRating" to starRating,
            "videoUrl" to videoUrl,
            "adChoiceUrl" to adChoiceUrl,
            "adSource" to adSource,
            "appName" to appName,
            "authorName" to authorName,
            "packageSizeBytes" to packageSizeBytes,
            "permissionsUrl" to permissionsUrl,
            "privacyAgreement" to privacyAgreement,
            "versionName" to versionName,
            "advertiserName" to advertiserName,
            "picUrls" to picUrls,
            "nativeAd" to nativeAd
        )
    }

    fun mapCustomParamsNotDouble(params: Map<String, Any?>?): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        if (params == null) return result
        for ((key, value) in params) {
            if (value != null) {
                result[key] = if (value is Double) value.toInt() else value
            }
        }
        return result
    }

    fun mapValidator(message: String, status: Boolean): Map<String, Any> {
        return mapOf(
            "message" to message,
            "status" to status
        )
    }
}