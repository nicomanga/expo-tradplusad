import { NativeModule, requireNativeModule } from 'expo';
import { TPAdError, TPAdInfo } from './ExpoType.types';

type ExpoSplashEvents = {
  onAdLoaded(params: { tpBaseAd: any, tpAdInfo: TPAdInfo }): void;
  onAdShowFailed(params: { tpAdError: TPAdError, tpAdInfo: TPAdInfo }): void;
  onAdLoadFailed(params: { tpAdError: TPAdError }): void;
  onAdImpression(params: { tpAdInfo: TPAdInfo }): void;
  onAdClosed(params: { tpAdInfo: TPAdInfo }): void;
  onAdClicked(params: { tpAdInfo: TPAdInfo }): void;
  onZoomOutStart(params: { tpAdInfo: TPAdInfo }): void;
  onZoomOutEnd(params: { tpAdInfo: TPAdInfo }): void;
}


declare class ExpoSplash extends NativeModule<ExpoSplashEvents> {
  initAd(placementId: string): string;
  loadAd(): void;
  onClean(): void;
  entryAdScenario(entryAdScenario: string): void;
  isReady(): boolean
  showAd(): void;
  onDestroy(): void;
}

const ExpoSplashAd = requireNativeModule<ExpoSplash>('ExpoSplashAd');
export default ExpoSplashAd
