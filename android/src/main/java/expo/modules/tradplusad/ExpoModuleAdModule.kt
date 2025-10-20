package expo.modules.tradplusad

import android.content.Context
import com.tradplus.ads.base.common.TPPrivacyManager
import com.tradplus.ads.open.TradPlusSdk
import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.CodedException
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.tradplusad.utils.Utils

class ExpoModuleAdModule: Module() {


    private val context
        get() = requireNotNull(appContext.reactContext)

    override fun definition() = ModuleDefinition {

        Name("ExpoModuleAd")

        Events("onInitSuccess", "GlobalImpressionListener")

        Function("initSdk") { initSdk: String ->
            TradPlusSdk.setTradPlusInitListener {
                sendEvent("onInitSuccess", mapOf(
                    "initSdk" to true
                ))
            }
            TradPlusSdk.initSdk(context, initSdk)
        }

        Function("getIsInit") {
            return@Function TradPlusSdk.getIsInit()
        }

        Function("setIsCNLanguageLog") { cn: Boolean ->
            TradPlusSdk.setIsCNLanguageLog(cn)
        }

        AsyncFunction("checkCurrentArea") { promise: Promise ->
            TradPlusSdk.checkCurrentArea(context, object: TPPrivacyManager.OnPrivacyRegionListener {
                override fun onSuccess(p0: Boolean, p1: Boolean, p2: Boolean) {
                    promise.resolve(mapOf(
                        "p0" to p0,
                        "p1" to p1,
                        "p2" to p2
                    ))
                }
                override fun onFailed() {
                    promise.reject(CodedException("unknown"))
                }
            })
        }

        Function("setGDPRDataCollection") { gdpr: Int ->
            TradPlusSdk.setGDPRDataCollection(context, gdpr)
        }

        Function("getGDPRDataCollection") {
            return@Function TradPlusSdk.getGDPRDataCollection(context)
        }

        Function("setLGPDConsent") { gdpr: Int ->
            TradPlusSdk.setLGPDConsent(context, gdpr)
        }

        Function("getLGPDConsent") {
            return@Function TradPlusSdk.getLGPDConsent(context)
        }

        Function("setEUTraffic") { gdpr: Boolean ->
            TradPlusSdk.setEUTraffic(context, gdpr)
        }

        Function("isEUTraffic") {
            return@Function TradPlusSdk.isEUTraffic(context)
        }

        Function("setCalifornia") { gdpr: Boolean ->
            TradPlusSdk.setCalifornia(context, gdpr)
        }

        Function("isCalifornia") {
            return@Function TradPlusSdk.isCalifornia(context)
        }

        Function("setIsFirstShowGDPR") { gdpr: Boolean ->
            TradPlusSdk.setIsFirstShowGDPR(context, gdpr)
        }

        Function("isFirstShowGDPR") {
            return@Function TradPlusSdk.isFirstShowGDPR(context)
        }

        Function("setCOPPAIsAgeRestrictedUser") { gdpr: Boolean ->
            TradPlusSdk.setCOPPAIsAgeRestrictedUser(context, gdpr)
        }

        Function("isCOPPAAgeRestrictedUser") {
            return@Function TradPlusSdk.isCOPPAAgeRestrictedUser(context)
        }

        Function("setGDPRChild") { gdpr: Boolean ->
            TradPlusSdk.setGDPRChild(context, gdpr)
        }

        Function("getGDPRChild") {
            return@Function TradPlusSdk.getGDPRChild(context)
        }

        Function("setCCPADoNotSell") { gdpr: Boolean ->
            TradPlusSdk.setCCPADoNotSell(context, gdpr)
        }

        Function("isCCPADoNotSell") {
            return@Function TradPlusSdk.isCCPADoNotSell(context)
        }

        Function("setAutoExpiration") { auto: Boolean ->
            TradPlusSdk.setAutoExpiration(auto)
        }

        Function("setOpenDelayLoadAds") { delay: Boolean ->
            TradPlusSdk.setOpenDelayLoadAds(delay)
        }

        Function("checkAutoExpiration") {
            TradPlusSdk.checkAutoExpiration()
        }

        Function("setAuthUID") { gdpr: Boolean ->
            TradPlusSdk.setAuthUID(context, gdpr)
        }

        Function("getAuthUID") {
            return@Function TradPlusSdk.getAuthUID(context)
        }

        Function("setDevOAID") { gdpr: String ->
            TradPlusSdk.setDevOAID(context, gdpr)
        }

        Function("getDevOaid") {
            return@Function TradPlusSdk.getDevOaid(context)
        }

        Function("setDevAllowTracking") { gdpr: Boolean ->
            TradPlusSdk.setDevAllowTracking(gdpr)
        }

        Function("isDevAllowTracking") {
            return@Function TradPlusSdk.isDevAllowTracking()
        }

        Function("setCnServer") { gdpr: Boolean ->
            TradPlusSdk.setCnServer(gdpr)
        }

        Function("setAllowMessagePush") { gdpr: Boolean ->
            TradPlusSdk.setCnServer(gdpr)
        }

        Function("isAllowMessagePush") {
            return@Function TradPlusSdk.isAllowMessagePush()
        }

        Function("isOpenPersonalizedAd") {
            return@Function TradPlusSdk.isOpenPersonalizedAd()
        }

        Function("setOpenPersonalizedAd") { gdpr: Boolean ->
            TradPlusSdk.setOpenPersonalizedAd(gdpr)
        }

        Function("isPrivacyUserAgree") {
            return@Function TradPlusSdk.isPrivacyUserAgree()
        }

        Function("setSettingDataParam") { params: Map<String, Any?> ->
            TradPlusSdk.setSettingDataParam(params)
        }

        Function("setPrivacyUserAgree") { gdpr: Boolean ->
            TradPlusSdk.setPrivacyUserAgree(gdpr)
        }

        Function("setGlobalImpressionListener") {
            TradPlusSdk.setGlobalImpressionListener { p0 ->
                sendEvent(
                    "GlobalImpressionListener",
                    Utils.mapToAdInfo(p0)
                )
            }
        }

        Function("clearCache") { s: String ->
            TradPlusSdk.clearCache(s)
        }

        Function("clearCacheByShareId") { s: String ->
            TradPlusSdk.clearCacheByShareId(s)
        }

        Function("setMaxDatabaseSize") { s: Long ->
            TradPlusSdk.setMaxDatabaseSize(s)
        }

        Function("clearFilters") {
           TradPlusSdk.clearFilters()
        }

        Function("getISO") {
            return@Function TradPlusSdk.getISO()
        }

        Function("getSdkVersion") {
            return@Function TradPlusSdk.getSdkVersion()
        }

        Function("setTestCustomId") { customId: String ->
            TradPlusSdk.setTestCustomId(customId)
        }

        Function("setPAConsent") { customId: Int ->
            TradPlusSdk.setPAConsent(customId)
        }


    }



}