import { NativeModule, requireNativeModule  } from 'expo'
import { TPAdInfo } from './ExpoType.types'

type ExpoModuleEvents = {
  onInitSuccess: (params: { initSdk: boolean }) => void
  GlobalImpressionListener: (params: TPAdInfo) => void
}

declare class ExpoModule extends NativeModule<ExpoModuleEvents> {
    initSdk(applicationID: string): void;
    getIsInit(): boolean
    setIsCNLanguageLog(cn: Boolean): void;
    checkCurrentArea(): Promise<{ p0: boolean, p1: boolean; p2: boolean; }>
    setGDPRDataCollection(i: number): void;
    getGDPRDataCollection(): boolean;
    setLGPDConsent(i: number): void;
    getLGPDConsent(): number;
    setEUTraffic(i: boolean): void;
    isEUTraffic(): boolean;
    setCalifornia(i: boolean): void;
    isCalifornia(): boolean;
    setIsFirstShowGDPR(i: boolean): void;
    isFirstShowGDPR(): boolean;
    setCOPPAIsAgeRestrictedUser(i: boolean): void;
    isCOPPAAgeRestrictedUser(): boolean;
    setGDPRChild(i: boolean): void;
    getGDPRChild(): boolean;
    setCCPADoNotSell(i: boolean): void;
    isCCPADoNotSell(): boolean;
    setAutoExpiration(i: boolean): void;
    setOpenDelayLoadAds(i: boolean): void;
    checkAutoExpiration(): void;
    setAuthUID(i: boolean): void;
    getAuthUID(): boolean;
    setDevOAID(i: string): void;
    getDevOaid(): string;
    setDevAllowTracking(i: boolean): void;
    isDevAllowTracking(): boolean;
    setCnServer(i: boolean): void;
    setAllowMessagePush(i: boolean): void;
    isAllowMessagePush(): boolean;
    isOpenPersonalizedAd(): boolean;
    setOpenPersonalizedAd(i: boolean): void;
    isPrivacyUserAgree(): boolean;
    setSettingDataParam(params: { [key: string]: any }): void;
    setPrivacyUserAgree(i: boolean): void;
    setGlobalImpressionListener(): void;
    clearCache(): void;
    clearCacheByShareId(i: string): void;
    setMaxDatabaseSize(i: number): void;
    clearFilters(): void;
    getISO(): string;
    getSdkVersion(): string;
    setTestCustomId(customId: string): void;
    setPAConsent(customId: number): void;
}

const ExpoModuleAd = requireNativeModule<ExpoModule>('ExpoModuleAd')

export default ExpoModuleAd