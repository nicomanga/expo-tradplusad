import { createContext } from "react";
import { View } from "react-native";
import { NativeAdViewMap } from "../ExpoType.types";

type NativeRef = {
    requestAd(): void
    check(): void
    showAd(): void
    updateAssets(assetName: string, _tag: number): void
} & View

const NativeContext = createContext<{
    nativeRef?: React.RefObject<NativeRef | null>
    tpBaseAd?: NativeAdViewMap
}>({ });

export { 
    NativeRef,
    NativeContext 
}