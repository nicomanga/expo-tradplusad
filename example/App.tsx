// import { useEvent } from 'expo';
// import ExpoTradplusad, { ExpoTradplusadView } from 'expo-tradplusad';
// import ExpoReward from 'expo-tradplusad/ExpoRewardModule'
// import ExpoSplash from 'expo-tradplusad/ExpoSplashModule'
// // import ExpoNativeView from 'expo-tradplusad/ExpoNativeModule'
// import { useCallback, useEffect, useImperativeHandle, useRef, useState } from 'react';
// import { requireNativeViewManager } from 'expo-modules-core'
// import { Button, findNodeHandle, Image, SafeAreaView, ScrollView, StyleProp, Text, TextProps, ToastAndroid, View } from 'react-native';
// import { ViewStyle } from 'react-native';
// import React from 'react';
// import { ImageProps } from 'react-native/types_generated/index';

// type NativeAdViewProps = {
//   unitId: string;
//   style?: StyleProp<ViewStyle>;
//   children?: React.ReactNode;
// }

// type NativeAdViewEvent = {

// }

// const NativeAdView = requireNativeViewManager<{
//   ref?: any
//   unitId: string;
//   style?: StyleProp<ViewStyle>;
//   children?: React.ReactNode;
//   onResponse?: (e: any) => void
// }>('ExpoNative');

// const AppNative = React.createContext<{
//   appRef?: any;
//   state: {[key: string]: any}
// }>({
//   state: {}
// })

// const NativeAd = React.forwardRef<any, NativeAdViewProps>((props, _ref) => {

//     const [state, setState] = useState<{ [key: string]: unknown }>({});

//     const appRef = React.useRef(null)

//     useImperativeHandle(_ref, () => ({
//       loadAd() {
//         console.info('loadAd')
//       }
//     }));

//     const onResponse = useCallback((e: { nativeEvent: {[key: string]: unknown} }) => {
//         setState(e.nativeEvent);
//     }, [])

//     return (
//         <AppNative.Provider value={{ state, appRef: appRef }}>
//             <NativeAdView 
//               ref={appRef} 
//               onResponse={onResponse}
//               {...props} 

//             />
//         </AppNative.Provider>
//     )
// })

// const NativeTitle: React.ComponentType<TextProps> = ({
//   children,
//   ...props
// }) => {

//     const titleRef = useRef<Text>(null)

//     const { state, appRef } = React.use(AppNative);

//     useEffect(() => {
//         if (titleRef.current && appRef.current) {
//             const titleId = findNodeHandle(titleRef.current);
//             console.info(titleId)
//             appRef.current.titleId(titleId)
//         }
//     }, [])

//     return <Text 
//       {...props}
//       ref={titleRef}
//     >{state.title ? state.title : children}</Text>
// }

// const MediaView: React.ComponentType<ImageProps> = (
//   props
// ) => {



//     return <Image {...props} />
// }

// export default function App() {
//   const [state, setState] = useState(false);
//   const [state1, setState1] = useState(false);
//   const [native, setNative] = useState(false)
//   const onChangePayload = useEvent(ExpoTradplusad, "onInitSuccess");

//   // const onLoad = useEvent(ExpoReward, "onAdLoaded");

//   useEffect(() => {
//     // console.info(ExpoTradplusad.getIsInit());
//     // if (!ExpoTradplusad.getIsInit()) {
//     //   ExpoTradplusad.initSdk("0807F46533A1818EAEDF3E483A48E604")
//     // }
//   }, [])

//   return (
//     <SafeAreaView style={styles.container}>
//       <ScrollView style={styles.container}>
//         <Text style={styles.header}>Module API Example</Text>

//         <Group name='native'>
//           <NativeAd unitId='67DCFDD50DAA6C72176859126D6A3A84'>
//             <View>
//               <NativeTitle>khalis</NativeTitle>
//               <MediaView style={{ height: 250, width: 300 }} />
//               <Text>Hello</Text>
//               <Text>Hello</Text>
//             </View>
//           </NativeAd>
//         </Group>

//         <Group name='Prepare'>
//           <Button
//             title='Run'
//             onPress={() => {
//               console.info(ExpoTradplusad.getIsInit());
//               if (!ExpoTradplusad.getIsInit()) {
//                 ExpoTradplusad.initSdk("0807F46533A1818EAEDF3E483A48E604")
//               }
//             }}
//           />
//         </Group>

//         <Group name='Reward'>
//           <Button
//             title='createAd'
//             onPress={() => {
//               ExpoReward.initAd('7F5B1236C411E12AF721E669A0B7A4E3');
//             }}
//           />
//           <Button
//             title='loadAd'
//             onPress={() => {
//               ExpoReward.loadAd();
//             }}
//           />
//           <Button
//             title='isReady'
//             onPress={() => {
//               ToastAndroid.show(`status : ${ExpoReward.isReady()}`, 500);
//             }}
//           />
//           <Button
//             title='showAd'
//             onPress={() => {
//               ExpoReward.showAd()
//             }}
//           />
//         </Group>

//         <Group name='Splash'>
//           <Button
//             title='createAd'
//             onPress={() => {
//               ExpoSplash.initAd('125315F95F59A47F89A68C805539A312');
//             }}
//           />
//           <Button
//             title='loadAd'
//             onPress={() => {
//               ExpoSplash.loadAd();
//             }}
//           />
//           <Button
//             title='isReady'
//             onPress={() => {
//               ToastAndroid.show(`status : ${ExpoSplash.isReady()}`, 500);
//             }}
//           />
//           <Button
//             title='showAd'
//             onPress={() => {
//               ExpoSplash.showAd()
//             }}
//           />
//         </Group>



//         <Group name="Async functions">
//           <Button
//             title="Set value"
//             onPress={async () => {
//               // // await ExpoTradplusad.setValueAsync('Hello from JS!');
//               // ExpoTradplusad.initSdk('0807F46533A1818EAEDF3E483A48E604');
//               // // 


//               // // console.info(ExpoTradplusad)
//             }}
//           />
//         </Group>
//         <Group name="Async functions">
//           <Button
//             title="Load Reward"
//             onPress={async () => {
//               // await ExpoTradplusad.setValueAsync('Hello from JS!');
//               ExpoReward.initAd('7F5B1236C411E12AF721E669A0B7A4E3');
//               ExpoReward.loadAd();
//               // ExpoReward.initAd('B49C2A89E603D43599EE19285454F7C6');


//               // console.info(ExpoTradplusad)
//             }}
//           />
//         </Group>
//         <Group name="Async functions">
//           <Button
//             title="Ready"
//             onPress={async () => {
//               if (ExpoReward.isReady()) {
//                 ExpoReward.showAd();
//               }

//               // console.info(ExpoTradplusad)
//             }}
//           />
//         </Group>
//         <Group name="Events">
//           <Text>{onChangePayload?.value}</Text>
//         </Group>
//         <Group name="Views">
//           <Button
//             title='bannerAd'
//             onPress={() => {
//               if (ExpoTradplusad.getIsInit()) {
//                 setState(!state)
//               }
//             }}
//           />
//           <View style={{ flex: 1, height: state ? 320 : 0, overflow: 'hidden' }}>
//             <ExpoTradplusadView
//               ref={async (ref) => {

//                 if (ref) {
//                   if (typeof ref.showAd === 'function') {
//                     setInterval(() => {
//                       ref.showAd()
//                     }, 500)
//                   }
//                 }


//               }}
//               unitId='59F4959A62763BDFEED9A1B38A41E410'
//               style={{
//                 flex: 1
//               }}

//             />
//           </View>
//         </Group>
//         <Group name="Views1">
//           <Button
//             title='bannerAd'
//             onPress={() => {
//               if (ExpoTradplusad.getIsInit()) {
//                 setState1(!state1)
//               }
//             }}
//           />
//           <View style={{ flex: 1, height: state1 ? 320 : 0, overflow: 'hidden' }}>
//             <ExpoTradplusadView
//               ref={async (ref) => {

//                 if (ref) {
//                   if (typeof ref.showAd === 'function') {
//                     setInterval(() => {
//                       ref.showAd()
//                     }, 500)
//                   }
//                 }


//               }}
//               unitId='1CAD6EDD1FEF16117993CE1A0104471B'
//               style={{
//                 flex: 1
//               }}
//             />
//           </View>
//         </Group>
//       </ScrollView>
//     </SafeAreaView>
//   );
// }

// function Group(props: { name: string; children: React.ReactNode }) {
//   return (
//     <View style={styles.group}>
//       <Text style={styles.groupHeader}>{props.name}</Text>
//       {props.children}
//     </View>
//   );
// }

// const styles = {
//   header: {
//     fontSize: 30,
//     margin: 20,
//   },
//   groupHeader: {
//     fontSize: 20,
//     marginBottom: 20,
//   },
//   group: {
//     margin: 20,
//     backgroundColor: '#fff',
//     borderRadius: 10,
//     padding: 20,
//   },
//   container: {
//     flex: 1,
//     backgroundColor: '#eee',
//   },
//   view: {
//     flex: 1,
//     height: 320,
//   },
// };


import React, { useEffect, useRef } from 'react';
import { AppState, Button, Dimensions, Image, ScrollView, Text, View } from 'react-native';
import ExpoModule from 'expo-tradplusad'
import { useEvent } from 'expo';
import BannerAd from './BannerAd';
import RewardAd from './RewardAd';
import Consent from './Consent';
import SplashAd from './SplashAd';
import { NativeRef } from 'expo-tradplusad/nativeAd/NativeContext';
import Native from './Native';
import NativeBanner from './Native-Banner';

const App = () => {

    

    const onInitAd = useEvent(ExpoModule, 'onInitSuccess')

    useEffect(() => {
        if (ExpoModule.getIsInit()) {
            ExpoModule.initSdk('0807F46533A1818EAEDF3E483A48E604');
        }
    }, [])

    return (
        <View style={{ flex: 1, paddingTop: 58, backgroundColor: 'white' }}>
            {onInitAd?.initSdk ? (
                <Text style={{ fontSize: 16, fontWeight: 'bold' }}>Tradplus {onInitAd.initSdk}</Text>
            ) : <Text>Tradplus</Text>}
            <Text>{onInitAd?.initSdk}</Text>
            <View style={{ height: 1, width: '100%', backgroundColor: 'black', marginTop: 5 }} />
            <ScrollView>
                <NativeBanner/>

                {/* <NativeView
                    ref={nativeRef}
                    placementId='67DCFDD50DAA6C72176859126D6A3A84'
                    customParams={{
                        'Admob_Adchoices': 1
                    }}
                >
                    <View style={{ padding: 5, gap: 5 }}>
                        <NativeMediaView
                            source={{
                                uri: 'https://s4.ihlv1.xyz/images/20200825/5f4494c81afa9_5f4494cb3d291.jpg',
                                headers: {
                                    referer: 'https://lovehug.net'
                                }
                            }}
                            style={{ height: 250, width: '100%' }}
                        />
                        <NativeTitle>Khalis</NativeTitle>
                        <NativeIconView
                            source={{
                                uri: 'https://s4.ihlv1.xyz/images/20200825/5f4494c81afa9_5f4494cb3d291.jpg',
                                headers: {
                                    referer: 'https://lovehug.net'
                                }
                            }}
                            style={{ height: 50, width: 50 }}
                        />
                        <NativeSubtitle>subtitle</NativeSubtitle>
                        <NativeButton
                            style={{
                                height: 50,
                                backgroundColor: '#000',
                                justifyContent: 'center',
                                alignItems: 'center',
                                borderRadius: 5,
                            }}
                            textProps={{
                                style: {
                                    color: '#fff',
                                    fontSize: 18,
                                    fontWeight: 'bold'
                                }
                            }}
                        />
                        <NativeAdChoiceView
                            style={{
                                height: 100,
                                width: 100,
                                // backgroundColor: 'red',
                                position: 'absolute',
                                right: 5,
                                top: 30,
                            }}
                        />
                    </View>
                </NativeView> */}

                <Consent />
                {/* <BannerAd  /> */}
                <RewardAd />
                <SplashAd />
            </ScrollView>
        </View>
    )
}

export default App;