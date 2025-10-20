import { createContext } from "react";
import { NativeAdViewMap } from "../ExpoType.types";

interface NativeAdRef {
    updateAssets(assetName: string, _tag: number): void
    showAd: () => void;
    check: () => void;
}

interface NativeContextProps {
    nativeRef?: React.RefObject<NativeAdRef | null>
    assets?: NativeAdViewMap | null
}

const NativeAdContext = createContext<NativeContextProps>({});

export {
    NativeAdRef,
    NativeContextProps,
    NativeAdContext
}