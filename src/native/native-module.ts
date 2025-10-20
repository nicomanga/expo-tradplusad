import { NativeModule, requireNativeModule } from 'expo-modules-core'
import { NativeAdViewMap, TPAdError, TPAdInfo } from '../ExpoType.types';

type NativeOption = {
    unitId: string
    size?: { width: number, height: number };
    entryAdScenario?: string;
    customParams?: { [key: string]: any }
}

interface NativeEventBody {
    unitId: string
    tpAdInfo: TPAdInfo
    tpBaseAd: NativeAdViewMap
    tpAdError: TPAdError
}

type NativeEvent = {
    onAdLoaded(body: Omit<NativeEventBody, "tpAdError">): void;
    // onAdShowFailed(body: Omit<NativeEventBody, "tpBaseAd">): void;
    // onAdLoadFailed(params: { tpAdError: TPAdError }): void;
    // onAdImpression(params: { tpAdInfo: TPAdInfo }): void;
    // onAdClosed(params: { tpAdInfo: TPAdInfo }): void;
    // onAdClicked(params: { tpAdInfo: TPAdInfo }): void;
    // onZoomOutStart(params: { tpAdInfo: TPAdInfo }): void;
    // onZoomOutEnd(params: { tpAdInfo: TPAdInfo }): void;
}

declare class NativeType extends NativeModule<NativeEvent> {
    requestAd(option: NativeOption): Promise<string>;
    loadAd(unitId: string): void;
    isReady(unitId: string): boolean
    assets(unitId: string): NativeAdViewMap;
}

const NativeAd = requireNativeModule<NativeType>('nativeAd')

export { NativeAd }