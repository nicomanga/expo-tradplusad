import { NativeModule, requireNativeModule  } from 'expo'

export enum DebugGeography {
    DEBUG_GEOGRAPHY_DISABLED = 0,
    DEBUG_GEOGRAPHY_EEA = 1
}

declare class ExpoConsent extends NativeModule {
    requestInfoUpdate(params?: {
        testDeviceIdentifiers?: string[]
        debugGeography?: DebugGeography;
        tagForUnderAgeOfConsent?: boolean
    }): Promise<any>;
    showForm(): Promise<any>;
    showPrivacyOptionsForm(): Promise<any>;
    loadAndShowConsentFormIfRequired(): Promise<any>;
    getConsentInfo(): Promise<any>;
    reset(): void;
    getTCString(): Promise<any>;
    getPurposeConsents(): Promise<any>;
    getPurposeLegitimateInterests(): Promise<any>
}

const ExpoConsentUMP = requireNativeModule<ExpoConsent>('ExpoConsent')

export default ExpoConsentUMP