import { requireNativeViewManager } from 'expo-modules-core';
import React, { useCallback, useImperativeHandle, useRef, useState } from 'react';
import { NativeContext, NativeRef } from './NativeContext';
import { 
   // LayoutChangeEvent, 
    ViewProps } from 'react-native';
import { NativeAdViewMap, TPAdError, TPAdInfo } from '../ExpoType.types';

type NativeForwardRef = {
    showAd(): void
    check(): void;
    requestAd(): void
}

type Event = {
    tpAdInfo: TPAdInfo
    tpAdError: TPAdError;
    tpBaseAd: NativeAdViewMap
}

type NativeEvent = {
    onAdLoaded?(event: { nativeEvent: Omit<Event, 'tpAdError'> }): void
    onAdClicked?(event: { nativeEvent: Omit<Event, 'tpAdError' | 'tpAdError'> }): void
    onValidator?(event: { nativeEvent: { status: boolean, message: string } }): void;
}

type NativeProps = {
    ref?: React.Ref<NativeRef> | undefined
    request: {
        unitId: string;
        size?: {
            height: number;
            width: number;
        }
        entryAdScenario?: string;
        customParams?: { [key: string]: any }
    }
    children: React.ReactNode
} & ViewProps

type NativeComponentProps = NativeEvent & NativeProps

const Native: React.ComponentType<NativeEvent & NativeProps> = requireNativeViewManager('ExpoNativeAd')

const NativeAd = React.forwardRef<NativeForwardRef, NativeComponentProps>(({
    children,
    onAdLoaded,
    ...props
}, ref) => {

    const [tpBaseAd, setTPBaseAd] = useState<NativeAdViewMap>()
    const nativeRef = useRef<NativeRef>(null)

    const showAd = useCallback(() => {
        nativeRef.current?.showAd()
    }, []);

    const requestAd = useCallback(() => {
        nativeRef.current?.requestAd()
    }, [])
    
    useImperativeHandle(ref, () => ({ 
        showAd,
        requestAd,
        check() {
            if (nativeRef.current) {
                nativeRef.current.check()
            }
        },
    }))
    
    
    return (
        <NativeContext.Provider value={{ nativeRef, tpBaseAd }}>
            <Native {...props} 
                ref={nativeRef} 
                onAdLoaded={(event) => {
                    onAdLoaded?.(event)
                    setTPBaseAd(event.nativeEvent.tpBaseAd)
                }}
            >
                {children}
            </Native>
        </NativeContext.Provider>
    )
})

export { 
    NativeForwardRef,
    NativeAd
}