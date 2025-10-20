import { useContext, useRef } from "react"
import { findNodeHandle, Image, ImageProps, Text, TextProps } from "react-native"
import { NativeContext } from "./NativeContext"


export const NativeTitle: React.ComponentType<TextProps> = ({
    children,
    ...props
}) => {

    const titleRef = useRef<Text>(null)
    const { nativeRef, tpBaseAd } = useContext(NativeContext)

    const onLayout = () => {
        const node = findNodeHandle(titleRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('title-id', node)
        }
    }
    
    return (
        <Text {...props} 
            ref={titleRef} 
            onLayout={onLayout}
        >
            {tpBaseAd?.title ? tpBaseAd.title : children}
        </Text>
    )
}

export const NativeSubtitle: React.ComponentType<TextProps> = ({
    children,
    ...props
}) => {
    const subtitleRef = useRef<Text>(null)
    const { nativeRef, tpBaseAd } = useContext(NativeContext)

    const onLayout = () => {
        const node = findNodeHandle(subtitleRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('subtitle-id', node)
        }
    }

    return (
        <Text {...props} 
            ref={subtitleRef} 
            onLayout={onLayout}
        >
            {tpBaseAd?.subtitle ? tpBaseAd.subtitle : children}
        </Text>
    )
}

export const NativeButton: React.ComponentType<TextProps> = ({
    children,
    ...props
}) => {

    const ctaRef = useRef<Text>(null)
    const { nativeRef, tpBaseAd } = useContext(NativeContext)

    const onLayout = () => {
        const node = findNodeHandle(ctaRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('cta-id', node)
        }
    }

    return <Text {...props} ref={ctaRef} onLayout={onLayout}>{
        tpBaseAd?.callToAction ? 
        tpBaseAd.callToAction :
        children
    }</Text>
}

export const NativeIcon: React.ComponentType<ImageProps> = ({ ...props }) => {

    const iconRef = useRef<Image>(null)
    const { nativeRef } = useContext(NativeContext)

    const onLayout = () => {
        const node = findNodeHandle(iconRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('icon-id', node)
        }
    }

    return (<Image {...props} ref={iconRef} onLayout={onLayout} />)
}

export const NativeMedia: React.ComponentType<ImageProps> = ({ ...props }) => {
    const mediaRef = useRef<Image>(null)
    const { nativeRef } = useContext(NativeContext)
    const onLayout = () => {
        const node = findNodeHandle(mediaRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('media-id', node)
        }
    }
    return (<Image {...props} ref={mediaRef} onLayout={onLayout} />)
}

export const NativeChoice: React.ComponentType<ImageProps> = ({ ...props }) => {
    const choiceRef = useRef<Image>(null)
    const { nativeRef } = useContext(NativeContext)

    const onLayout = () => {
        const node = findNodeHandle(choiceRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('choice-id', node)
        }
    }
    return (<Image {...props} ref={choiceRef} onLayout={onLayout} />)
}

//choice-icon-id

export const NativeChoiceIcon: React.ComponentType<ImageProps> = ({ ...props }) => {
    const choiceRef = useRef<Image>(null)
    const { nativeRef } = useContext(NativeContext)

    const onLayout = () => {
        const node = findNodeHandle(choiceRef.current)
        if (node) {
            nativeRef?.current?.updateAssets('choice-icon-id', node)
        }
    }
    return (<Image {...props} ref={choiceRef} onLayout={onLayout} />)
}