import React, { useContext, useRef } from "react"
import { NativeAdContext, NativeAdRef } from "./native-context"
import { findNodeHandle, Image, ImageProps, Text, TextProps } from "react-native";

const onUpdateTagId = (
    nativeRef: React.RefObject<NativeAdRef | null> | undefined,
    componentOrHandle: null | number | React.Component<any, any> | React.ComponentClass<any>, 
    assetName: string
) => {
    const node = findNodeHandle(componentOrHandle);
    if (node && nativeRef && nativeRef.current) {
        nativeRef.current.updateAssets(assetName, node)
    }
}

const NativeTitle: React.FC<TextProps> = ({
    children,
    ...props
}) => {
    const ref = useRef<Text | Image>(null)
    const { assets, nativeRef } = useContext(NativeAdContext);
    const child = assets?.title ? assets.title : children

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'title-id')
    }

    return <Text {...props} ref={ref} onLayout={onLayout}>{child}</Text>
}

const NativeSubtitle: React.FC<TextProps> = ({
    children,
    ...props
}) => {

    const ref = useRef<Text | Image>(null)
    const { assets, nativeRef } = useContext(NativeAdContext);
    const child = assets?.subtitle ? assets.subtitle : children

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'subtitle-id')
    }

    return <Text {...props} ref={ref} onLayout={onLayout}>{child}</Text>
}

const NativeButton: React.FC<TextProps> = ({
    children,
    ...props
}) => {

    const ref = useRef<Text | Image>(null)
    const { assets, nativeRef } = useContext(NativeAdContext);
    const child = assets?.callToAction ? assets.callToAction : children

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'cta-id')
    }

    return <Text {...props} ref={ref} onLayout={onLayout}>{child}</Text>
}

const NativeIcon: React.FC<ImageProps> = ({
    ...props
}) => {

    const ref = useRef<Image>(null)
    const { nativeRef } = useContext(NativeAdContext);

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'icon-id')
    }

    return <Image {...props} ref={ref} onLayout={onLayout} />
}

const NativeMediaView: React.FC<ImageProps> = ({
    ...props
}) => {

    const ref = useRef<Image>(null)
    const { nativeRef } = useContext(NativeAdContext);

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'media-id')
    }

    return <Image {...props} ref={ref} onLayout={onLayout} />
}

const NativeChoice: React.FC<ImageProps> = ({
    ...props
}) => {

    const ref = useRef<Image>(null)
    const { nativeRef } = useContext(NativeAdContext);

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'choice-id')
    }

    return <Image {...props} ref={ref} onLayout={onLayout} />
}

const NativeIconChoice: React.FC<ImageProps> = ({
    ...props
}) => {

    const ref = useRef<Image>(null)
    const { nativeRef } = useContext(NativeAdContext);

    const onLayout = () => {
        onUpdateTagId(nativeRef, ref.current, 'choice-icon-id')
    }

    return <Image {...props} ref={ref} onLayout={onLayout} />
}

export {
    NativeTitle,
    NativeSubtitle,
    NativeButton,
    NativeIcon,
    NativeMediaView,
    NativeChoice,
    NativeIconChoice
}