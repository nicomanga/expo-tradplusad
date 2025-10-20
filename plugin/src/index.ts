import { AndroidConfig, ConfigPlugin, withAndroidManifest, withPlugins } from 'expo/config-plugins';

type PluginParameters = {
    androidAppId?: string;
    androidAdManager?: boolean;
    delayAppMeasurementInit?: boolean;
    optimizeInitialization?: boolean;
    optimizeAdLoading?: boolean;
    google_play_services_version?: string;
};

const addReplacingMainApplicationMetaDataItem = (
    manifest: AndroidConfig.Manifest.AndroidManifest,
    itemName: string,
    itemValue: string,
): AndroidConfig.Manifest.AndroidManifest => {
    AndroidConfig.Manifest.ensureToolsAvailable(manifest);

    const newItem = {
        $: {
            'android:name': itemName,
            'android:value': itemValue,
            'tools:replace': 'android:value',
        },
    } as AndroidConfig.Manifest.ManifestMetaData;

    const mainApplication = AndroidConfig.Manifest.getMainApplicationOrThrow(manifest);
    mainApplication['meta-data'] = mainApplication['meta-data'] ?? [];

    const existingItem = mainApplication['meta-data'].find(
        item => item.$['android:name'] === itemName,
    );

    if (existingItem) {
        existingItem.$['android:value'] = itemValue;
        existingItem.$['tools:replace' as keyof AndroidConfig.Manifest.ManifestMetaData['$']] =
            'android:value';
    } else {
        mainApplication['meta-data'].push(newItem);
    }

    return manifest;
}

const withAndroidAppId: ConfigPlugin<PluginParameters['androidAppId']> = (config, androidAppId) => {
    if (androidAppId === undefined) return config;

    return withAndroidManifest(config, config => {

        addReplacingMainApplicationMetaDataItem(
            config.modResults,
            'com.google.android.gms.ads.APPLICATION_ID',
            androidAppId
        );

        return config;
    });

}

const withAndroidAdLoadingOptimized: ConfigPlugin<PluginParameters['optimizeAdLoading']> = (
  config,
  optimizeAdLoading = true,
) => {
  return withAndroidManifest(config, config => {
    addReplacingMainApplicationMetaDataItem(
      config.modResults,
      'com.google.android.gms.ads.flag.OPTIMIZE_AD_LOADING',
      optimizeAdLoading.toString(),
    );

    return config;
  });
};


const withAndroidAdManager: ConfigPlugin<PluginParameters['androidAdManager']> = (config, androidAdManager = true) => {
    return withAndroidManifest(config, config => {

        if (androidAdManager) {
            addReplacingMainApplicationMetaDataItem(
                config.modResults,
                'com.google.android.gms.ads.AD_MANAGER_APP',
                "true"
            )
        }

        return config;
    });
}

const withAndroidInitializationOptimized: ConfigPlugin<
  PluginParameters['optimizeInitialization']
> = (config, optimizeInitialization = true) => {
  return withAndroidManifest(config, config => {
    addReplacingMainApplicationMetaDataItem(
      config.modResults,
      'com.google.android.gms.ads.flag.OPTIMIZE_INITIALIZATION',
      optimizeInitialization.toString(),
    );

    return config;
  });
};

const withAndroidAppMeasurementInitDelayed: ConfigPlugin<
  PluginParameters['delayAppMeasurementInit']
> = (config, delayAppMeasurementInit) => {
  if (delayAppMeasurementInit === undefined) return config;

  return withAndroidManifest(config, config => {
    addReplacingMainApplicationMetaDataItem(
      config.modResults,
      'com.google.android.gms.ads.DELAY_APP_MEASUREMENT_INIT',
      delayAppMeasurementInit.toString(),
    );

    return config;
  });
};

const withAndroidGmsVersion: ConfigPlugin<string> = (config, value = '@integer/google_play_services_version') => {
    return withAndroidManifest(config, config => {

        addReplacingMainApplicationMetaDataItem(
            config.modResults,
            'com.google.android.gms.version',
            value
        )

        return config;
    })
}

const withMyApiKey: ConfigPlugin<PluginParameters>  = (
    config,
    {
        androidAppId,
        androidAdManager,
        delayAppMeasurementInit,
        optimizeInitialization,
        optimizeAdLoading,
        google_play_services_version
    }
) => {
    
    if (androidAppId !== undefined) {
        console.warn(
        "No 'androidAppId' was provided. The native Google Mobile Ads SDK will crash on Android without it.",
        );
    }
    
    return withPlugins(config, [
        [withAndroidAppId, androidAppId],
        [withAndroidAdManager, androidAdManager],
        [withAndroidAppMeasurementInitDelayed, delayAppMeasurementInit],
        [withAndroidInitializationOptimized, optimizeInitialization],
        [withAndroidAdLoadingOptimized, optimizeAdLoading],
        [withAndroidGmsVersion, google_play_services_version]
    ]);
}

export default withMyApiKey;
