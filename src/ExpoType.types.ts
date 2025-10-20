export type TPAdInfo = {
  tpAdUnitId: string;
  trueAdUnitId: string;
  adSourceName: string;
  adNetworkId: string;
  adSourceId: string;
  ecpm: string;
  ecpmCny: string;
  ecpmLevel: string;
  ecpmPrecision: string;
  loadTime: number;
  rewardName: string;
  rewardNumber: number;
  isoCode: string;
  height: number;
  width: number;
  adViewHeight: number;
  adViewWidth: number;
  isBiddingNetwork: boolean;
  waterfallIndex: number;
  requestId: string;
  impressionId: string;
  subChannel: string;
  channel: string;
  sceneId: string;
  configString: string;
  format: string;
  videoProtocol: number;
  bucketId: string;
  segmentId: string;
  adSourcePlacementId: string;
  isExclusive: number;
  bannerW: number;
  bannerH: number;
  placementAdType: number;
  isBottom: boolean;
}

export type TPAdError = {
    message: string;
    code: number;
}

export type NativeAdViewMap = {
  aspectRatio?: number;
  sponsoredLabel?: string | null;
  huaweiAdInfoValue?: string | null;
  huaweiAdInfoKey?: string | null;
  huaweiAdInfoSeq?: number | null;
  lastUpdateTime?: string | null;
  nativeAdSocialContext?: string | null;
  mainImageUrl?: string | null;
  iconImageUrl?: string | null;
  clickUrl?: string | null;
  callToAction?: string | null;
  title?: string | null;
  subtitle?: string | null;
  starRating?: number | null;
  videoUrl?: string | null;
  adChoiceUrl?: string | null;
  adSource?: string | null;
  appName?: string | null;
  authorName?: string | null;
  packageSizeBytes?: number | null;
  permissionsUrl?: string | null;
  privacyAgreement?: string | null;
  versionName?: string | null;
  advertiserName?: string | null;
  picUrls?: string[] | null;

  /** Nested field from Kotlin: nativeAd */
  nativeAd?: {
    iconImage: boolean;
    iconImageUrl: string;
    adChoiceImage: boolean;
    mainImage: boolean;
    iconView: boolean;
    mediaView: boolean;
  };
}
