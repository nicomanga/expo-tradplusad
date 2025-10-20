import { requireNativeViewManager } from 'expo-modules-core'
import React, { useImperativeHandle, useRef } from 'react'
import { NativeAdContext, NativeAdRef } from './native-context'
import { NativeAdViewMap } from '../ExpoType.types';

const Native = requireNativeViewManager('nativeAd')

interface NativeViewRef {
    showAd: () => void
    check: () => void
}

interface NativeViewProps {
    requestAd?: string;
    children: React.ReactNode;
    assets?: NativeAdViewMap;
    onReady?: () => void
    onDisplay?: () => void;
}

const NativeView = React.forwardRef<NativeViewRef, NativeViewProps>(({ 
    children, 
    requestAd, 
    assets,
    ...props
 }, ref) => {

    const nativeRef = useRef<NativeAdRef>(null)


    useImperativeHandle(ref, () => {
        return {
            showAd: () => {
                nativeRef.current?.showAd()
            },
            check: () => {
                nativeRef.current?.check()
            }
        }
    })

    return (
        <NativeAdContext.Provider value={{ nativeRef, assets }}>
            <Native {...props} requestAd={requestAd} ref={nativeRef}>
                {children}
            </Native>
        </NativeAdContext.Provider>
    )
})

export { NativeView, NativeViewProps, NativeViewRef }

