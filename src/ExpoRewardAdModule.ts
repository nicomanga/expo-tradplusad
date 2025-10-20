import { NativeModule, requireNativeModule } from 'expo';
import { TPAdError, TPAdInfo } from './ExpoType.types';

type ExpoRewardEvents = {
  onAdLoaded(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdClicked(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdImpression(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdFailed(params: { placementId: string, tpAdError: TPAdError }): void
  onAdClosed(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdReward(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdVideoStart(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdVideoEnd(params: { placementId: string, tpAdInfo: TPAdInfo }): void
  onAdVideoError(params: { placementId: string, tpAdInfo: TPAdInfo, tpAdError: TPAdError }): void
}


declare class ExpoReward extends NativeModule<ExpoRewardEvents> {
  initAd(placementId: string): string;
  loadAd(placementId: string): void;
  reloadAd(placementId: string): void;
  clearCacheAd(placementId: string): void;
  entryAdScenario(placementId: string, entryAdScenario: string): void;
  isReady(placementId: string): boolean
  showAd(placementId: string): void;
  onDestroy(placementId: string): void;
}

const ExpoRewardAd = requireNativeModule<ExpoReward>('ExpoRewardAd');
export default ExpoRewardAd
