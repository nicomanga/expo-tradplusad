package expo.modules.tradplusad

import androidx.preference.PreferenceManager
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.PrivacyOptionsRequirementStatus
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import expo.modules.kotlin.Promise
import expo.modules.kotlin.functions.Queues
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.tradplusad.utils.ReactNativeModule


class ExpoConsentModule: Module() {

    private val activity get() = requireNotNull(appContext.throwingActivity)
    private val context get()  = requireNotNull(appContext.reactContext)

    private var consentInformation: ConsentInformation? = null

    private fun getConsentStatusString(consentStatus: Int?): String {
        return when (consentStatus) {
            ConsentInformation.ConsentStatus.REQUIRED -> "REQUIRED"
            ConsentInformation.ConsentStatus.NOT_REQUIRED -> "NOT_REQUIRED"
            ConsentInformation.ConsentStatus.OBTAINED -> "OBTAINED"
            ConsentInformation.ConsentStatus.UNKNOWN -> "UNKNOWN"
            else -> "UNKNOWN"
        }
    }

    private fun getPrivacyOptionsRequirementStatusString(
        privacyOptionsRequirementStatus: PrivacyOptionsRequirementStatus?
    ): String {
        return when (privacyOptionsRequirementStatus) {
            PrivacyOptionsRequirementStatus.REQUIRED -> "REQUIRED"
            PrivacyOptionsRequirementStatus.NOT_REQUIRED -> "NOT_REQUIRED"
            PrivacyOptionsRequirementStatus.UNKNOWN -> "UNKNOWN"
            else -> "UNKNOWN"
        }
    }

    private fun getConsentInformation(): Map<String, Any?> {
        val status = getConsentStatusString(consentInformation?.consentStatus)
        val canRequestAds = consentInformation?.canRequestAds()
        val privacyOptionsRequirementStatus = getPrivacyOptionsRequirementStatusString(
            consentInformation?.privacyOptionsRequirementStatus
        )
        val isConsentFormAvailable = consentInformation?.isConsentFormAvailable

        return mapOf(
            "status" to status,
            "canRequestAds" to canRequestAds,
            "privacyOptionsRequirementStatus" to privacyOptionsRequirementStatus,
            "isConsentFormAvailable" to isConsentFormAvailable
        )
    }

    override fun definition() = ModuleDefinition {
        Name("ExpoConsent")

        OnCreate {
            consentInformation = UserMessagingPlatform.getConsentInformation(context)
        }

        AsyncFunction("requestInfoUpdate") { option: ReactNativeModule.OptionConsents? ,it: Promise ->
            try {
                val paramsBuilder = ConsentRequestParameters.Builder()
                val debugSettingsBuilder = ConsentDebugSettings.Builder(context)

                if (option?.testDeviceIdentifiers != null) {
                    for (device in option.testDeviceIdentifiers) {
                        if (device != null) {
                            debugSettingsBuilder.addTestDeviceHashedId(device)
                        }
                    }
                }

                if (option?.debugGeography != null) {
                    debugSettingsBuilder.setDebugGeography(option.debugGeography)
                }

                paramsBuilder.setConsentDebugSettings(debugSettingsBuilder.build())

                if (option?.tagForUnderAgeOfConsent != null) {
                    paramsBuilder.setTagForUnderAgeOfConsent(option.tagForUnderAgeOfConsent)
                }

                val consentRequestParameters = paramsBuilder.build()

                consentInformation?.requestConsentInfoUpdate(
                    activity,
                    consentRequestParameters,
                    {
                        it.resolve(getConsentInformation())
                    },
                    { formError: FormError? ->
                        ReactNativeModule.rejectPromiseWithCodeAndMessage(
                            it, "consent-update-failed", formError!!.message
                        )
                    })

            } catch (e: Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-update-failed", e.message)
            }
        }

        AsyncFunction("showForm") { it: Promise ->
            try {
                activity.runOnUiThread {
                    UserMessagingPlatform.loadConsentForm(
                        context,
                        { consentForm: ConsentForm? ->
                            consentForm!!.show(
                                activity,
                                OnConsentFormDismissedListener { formError: FormError? ->
                                    if (formError != null) {
                                        ReactNativeModule.rejectPromiseWithCodeAndMessage(
                                            it,
                                            "consent-form-error",
                                            formError.message
                                        )
                                    } else {
                                        it.resolve(getConsentInformation())
                                    }
                                })
                        },
                        { formError: FormError? ->
                            ReactNativeModule.rejectPromiseWithCodeAndMessage(
                                it, "consent-form-error", formError!!.message
                            )
                        })
                }
            } catch (e: Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-form-error", e.message)
            }
        }.runOnQueue(Queues.MAIN)

        AsyncFunction("showPrivacyOptionsForm") { it: Promise ->
            try {
                activity.runOnUiThread {
                    UserMessagingPlatform.showPrivacyOptionsForm(
                        activity,
                        OnConsentFormDismissedListener { formError: FormError? ->
                            if (formError != null) {
                                ReactNativeModule.rejectPromiseWithCodeAndMessage(
                                    it,
                                    "privacy-options-form-error",
                                    formError.message
                                )
                            } else {
                                it.resolve(getConsentInformation())
                            }
                        })
                }
            } catch (e: Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-form-error", e.toString())
            }
        }.runOnQueue(Queues.MAIN)

        AsyncFunction("loadAndShowConsentFormIfRequired") { it: Promise ->
            try {
                activity.runOnUiThread {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                        activity,
                        OnConsentFormDismissedListener { formError: FormError? ->
                            if (formError != null) {
                                ReactNativeModule.rejectPromiseWithCodeAndMessage(
                                    it, "consent-form-error", formError.message
                                )
                            } else {
                                it.resolve(getConsentInformation())
                            }
                        })
                }

            } catch (e: Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-form-error", e.toString())
            }
        }.runOnQueue(Queues.MAIN)

        AsyncFunction("getConsentInfo") { it: Promise ->
            it.resolve(getConsentInformation())
        }

        Function("reset") {
            consentInformation?.reset()
        }

        AsyncFunction("getTCString") { it: Promise ->
            try {
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                // https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/blob/master/TCFv2/IAB%20Tech%20Lab%20-%20CMP%20API%20v2.md#in-app-details
                val tcString = prefs.getString("IABTCF_TCString", null)
                it.resolve(tcString)
            } catch (e: Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-string-error", e.toString())
            }
        }

        AsyncFunction("getPurposeConsents") { it: Promise ->
            try {
                val prefs =
                    PreferenceManager.getDefaultSharedPreferences(context)
                val purposeConsents: String = prefs.getString("IABTCF_PurposeConsents", "")!!
                it.resolve(purposeConsents)
            } catch (e: java.lang.Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-string-error", e.toString())
            }
        }

        AsyncFunction("getPurposeLegitimateInterests") { it: Promise ->
            try {
                val prefs =
                    PreferenceManager.getDefaultSharedPreferences(context)
                val purposeLegitimateInterests: String =
                    prefs.getString("IABTCF_PurposeLegitimateInterests", "")!!
                it.resolve(purposeLegitimateInterests)
            } catch (e: java.lang.Exception) {
                ReactNativeModule.rejectPromiseWithCodeAndMessage(it, "consent-string-error", e.toString())
            }
        }
    }
}