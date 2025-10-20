package expo.modules.tradplusad.utils

import expo.modules.kotlin.Promise
import expo.modules.kotlin.exception.CodedException
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

object ReactNativeModule {

    class NativeAdSize: Record {
        @Field
        val height: Int = 0

        @Field
        val width: Int = 0
    }

    class Native: Record {
        @Field
        val unitId: String? = null

        @Field
        val size: NativeAdSize? = null

        @Field
        val entryAdScenario: String? = null

        @Field
        val customParams: Map<String, Any?>? = emptyMap()
    }

    class NativeSize: Record {
        @Field
        val height: Int = 250
        @Field
        val width: Int = 300
    }

    class OptionConsents: Record {
        @Field
        val testDeviceIdentifiers: List<String?>? = null
        @Field
        val debugGeography: Int? = null
        @Field
        val tagForUnderAgeOfConsent: Boolean? = null
    }

    fun rejectPromiseWithCodeAndMessage(promise: Promise, code: String, message: String?) {
        promise.reject(CodedException(code, message, null))
    }
}