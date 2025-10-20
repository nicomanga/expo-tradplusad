import { requireNativeViewManager } from 'expo-modules-core';
import React, { useCallback, useImperativeHandle, useRef, useState } from 'react';
import {  findNodeHandle, Image, ImageProps, Pressable, PressableProps, Text, TextProps, View, ViewProps } from 'react-native';
import { NativeAdViewMap } from './ExpoType.types';

export type NativeRefProps = {
    requestAd(): void;
}

export type NativeRef = {
    title(id: number): void
    subtitle(id: number): void;
    cta(id: number): void
    media(id: number): void;
    icon(id: number): void
    ad_choice(id: number): void
} & NativeRefProps

export type NativeProps = {
    ref: React.Ref<NativeRef>
    children: React.ReactNode
    placementId: string;
    customParams?: { [key: string]: any }
    h?: number;
    w?: number;
} & ViewProps

export type NativeEvents = {
    onNative?: (event: { nativeEvent: NativeAdViewMap }) => void
}

const Native: React.ComponentType<NativeProps & NativeEvents> = requireNativeViewManager('ExpoNativeAd')

const NativeContext = React.createContext<{
    nativeRef?: React.RefObject<NativeRef | null>
    nativeMap?: NativeAdViewMap;
}>({});

const NativeView = React.forwardRef<NativeRefProps, NativeProps & NativeEvents>(({
    children,
    ...props
}, ref) => {

    const [nativeMap, setNativeMap] = useState<NativeAdViewMap | undefined>(undefined);

    const nativeRef = React.useRef<NativeRef>(null);

    const requestAd = useCallback(() => {
        nativeRef.current?.requestAd()
    }, [])

    useImperativeHandle(ref, () => ({ requestAd }))

    const onNative = (event: { nativeEvent: NativeAdViewMap }) => {
        setNativeMap(event.nativeEvent);
    }

    return (
         <Native {...props} ref={nativeRef} onNative={onNative}>
            <NativeContext.Provider value={{ nativeRef, nativeMap }} >
                    {children}
            </NativeContext.Provider>
        </Native>
        
    )
});

export const NativeTitle: React.ComponentType<TextProps> = ({
    children,
    ...props
}) => {

    const { nativeRef, nativeMap } = React.useContext(NativeContext);
    const titleRef = useRef<Text>(null)

    const onLayout = () => {
        const node = findNodeHandle(titleRef.current);
        if (nativeRef && nativeRef.current && node) {
            nativeRef.current.title(node)
        }
    }

    return (
        <Text {...props} ref={titleRef} onLayout={onLayout}>
            {nativeMap?.title ? nativeMap.title : children}
        </Text>
    )
}

export const NativeSubtitle: React.ComponentType<TextProps> = ({
    children,
    ...props
}) => {

    const { nativeRef, nativeMap } = React.useContext(NativeContext);
    const subtitleRef = useRef<Text>(null)

    const onLayout = () => {
        const node = findNodeHandle(subtitleRef.current);
        if (nativeRef && nativeRef.current && node) {
            nativeRef.current.subtitle(node)
        }
    }

    return (
        <Text {...props} ref={subtitleRef} onLayout={onLayout}>
            {nativeMap?.subtitle ? nativeMap.subtitle : children}
        </Text>
    )
}

export const NativeButton: React.ComponentType<
    Omit<PressableProps, 'children'> & 
    { textProps?: Omit<TextProps, 'children'> } &
    { title?: string }
> = ({
    title = 'BUTTON',
    textProps,
    ...props
}) => {

    const { nativeRef, nativeMap } = React.useContext(NativeContext);
    const buttonRef = useRef<View>(null)

    const onLayout = () => {
        const node = findNodeHandle(buttonRef.current);
        if (nativeRef && nativeRef.current && node) {
            nativeRef.current.cta(node)
        }
    }

    return (
        <Pressable {...props} ref={buttonRef} >
            <Text {...textProps} onLayout={onLayout}>
                {nativeMap?.callToAction ? nativeMap.callToAction : title}
            </Text>
        </Pressable>
    )
}

export const NativeMediaView: React.ComponentType<ImageProps> = ({
    ...props
}) => {

    const { nativeRef } = React.useContext(NativeContext);
    const mediaViewRef = useRef<Image>(null)

    const onLayout = () => {
        const node = findNodeHandle(mediaViewRef.current);
        if (nativeRef && nativeRef.current && node) {
            nativeRef.current.media(node)
        }
    }

    return <Image {...props} ref={mediaViewRef} onLayout={onLayout}  />
}


export const NativeIconView: React.ComponentType<ImageProps> = ({
    ...props
}) => {

    const { nativeRef } = React.useContext(NativeContext);
    const iconViewRef = useRef<Image>(null)

    const onLayout = () => {
        const node = findNodeHandle(iconViewRef.current);
        if (nativeRef && nativeRef.current && node) {
            nativeRef.current.icon(node)
        }
    }

    return <Image {...props} ref={iconViewRef} onLayout={onLayout}  />
}

export const NativeAdChoiceView: React.ComponentType<ViewProps> = ({
    ...props
}) => {
    const { nativeRef } = React.useContext(NativeContext);
    const choiceRef = useRef<View>(null)

    const onLayout = () => {
        const node = findNodeHandle(choiceRef.current);
         if (nativeRef && nativeRef.current && node) {
            nativeRef.current.ad_choice(node)
        }
    }
    
    return <View {...props} ref={choiceRef} onLayout={onLayout} />
}

export default NativeView

