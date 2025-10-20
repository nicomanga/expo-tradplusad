import { requireNativeViewManager } from 'expo-modules-core';
import React, { useImperativeHandle, useState } from 'react';
import { ViewProps } from 'react-native';
import { TPAdError, TPAdInfo } from './ExpoType.types';

type ExpoBannerRef = {
    ready: () => void
    showAd: () => void
}

type ExpoBannerProps = {
    placementId: string
    entryAdScenario?: string
    autoDestroy?: boolean;
    closeAutoShow?: boolean
    ref?: any;
    onAdLoaded?: (params: { nativeEvent: { tpAdInfo: TPAdInfo } }) => void;
    onAdLoadFailed?: (params: { nativeEvent: { tpAdError: TPAdError } }) => void;
    onAdShowFailed?: (params: { nativeEvent: {
         tpAdInfo: TPAdInfo
         tpAdError: TPAdError
    } }) => void;
    onAdImpression?: (params: { nativeEvent: { tpAdInfo: TPAdInfo } }) => void;
    onAdClosed?: (params: { nativeEvent: { tpAdInfo: TPAdInfo } }) => void;
    onAdClicked?: (params: { nativeEvent: { tpAdInfo: TPAdInfo } }) => void;
    onBannerRefreshed?: (params: { nativeEvent: { status: boolean } }) => void
} & ViewProps

const ExpoBanner: React.ComponentType<ExpoBannerProps & {
    onReady: (params: { nativeEvent: { ready: boolean } }) => void
}> = requireNativeViewManager('ExpoBannerAd')

const ExpoBannerView = React.forwardRef<Omit<ExpoBannerRef, 'ready'> & { ready: () => boolean }, ExpoBannerProps>((props, ref) => {

    const appRef = React.useRef<ExpoBannerRef>(null);
    const [ready, setReady] = useState(false);

    useImperativeHandle(ref, () => ({
        ready() {
            appRef.current?.ready()
            return ready
        },
        showAd() {
            appRef.current?.showAd();
            return
        }
    }))

    return <ExpoBanner 
        {...props} 
        onReady={(event) => {
            setReady(event.nativeEvent.ready)
        }}
        ref={appRef} 
    />
})

export default ExpoBannerView