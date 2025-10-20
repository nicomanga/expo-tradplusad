import { Dimensions, Pressable, StyleSheet, Text, ToastAndroid, View } from "react-native"
// import { NativeAd, NativeButton, NativeChoice, NativeChoiceIcon, NativeForwardRef, NativeIcon, NativeMedia, NativeSubtitle, NativeTitle, RewardAd as Reward } from 'expo-tradplusad'
import { useEffect, useRef, useState } from "react"
import { useEvent, useEventListener } from 'expo'
import { NativeAd, NativeAdViewMap, NativeButton, NativeChoice, NativeIcon, NativeIconChoice, NativeMediaView, NativeSubtitle, NativeTitle, NativeView } from "expo-tradplusad";

const unitId = '67DCFDD50DAA6C72176859126D6A3A84'
const source = { uri: 'https://s4.ihlv1.xyz/images/20200825/5f4494c81afa9_5f4494cb3d291.jpg' }

const Native = () => {
    const onAdLoaded = useEvent(NativeAd, "onAdLoaded");
    const [status, setStatus] = useState(false);
    const [assets, setAssets] = useState<NativeAdViewMap | {}>({})

    useEventListener(NativeAd, 'onAdLoaded', ({ tpBaseAd }) => {
        console.info(tpBaseAd);
    })

    const renderItem = () => {
        return (
            <NativeView assets={onAdLoaded?.tpBaseAd}>
                <View style={{ padding: 5, gap: 5 }}>
                    <View style={[styles.nativeHeaders]}>
                        <NativeIcon
                            style={styles.nativeIcon}
                            source={source}
                        />
                        <View style={{ flex: 1 }}>
                            <NativeTitle style={[styles.nativeTitle]}>title</NativeTitle>
                            {onAdLoaded?.tpBaseAd.advertiserName ? <Text style={styles.nativeTitle}>{onAdLoaded?.tpBaseAd.advertiserName}</Text> : null}
                            {onAdLoaded?.tpBaseAd.starRating ? <Text style={styles.nativeTitle}>{onAdLoaded?.tpBaseAd.starRating}</Text> : null}
                        </View>
                    </View>
                    <View>
                        <NativeMediaView
                            style={[styles.nativeMedia, { height: 250  }]}
                            source={source}
                        />
                        <NativeIconChoice
                            style={{
                                position: 'absolute',
                                height: 32,
                                width: 32,
                                left: 5,
                                top: 5,
                                backgroundColor: 'rgba(0,0,0,.5)'
                            }}
                        />
                        <NativeChoice
                            style={{
                                position: 'absolute',
                                height: 32,
                                width: 32,
                                right: 5,
                                top: 5,
                                backgroundColor: 'rgba(0,0,0,.5)'
                            }}
                        />
                    </View>
                    <NativeSubtitle>NativeSubtitle</NativeSubtitle>
                    <NativeButton style={[styles.nativeButton]}>Like</NativeButton>
                </View>
            </NativeView>
        )
    }

    return (
        <View style={styles.container}>
            <Text style={[styles.title]}>Native</Text>

            <Text>value {JSON.stringify(onAdLoaded)}</Text>

            <Pressable style={[styles.button]}
                onPress={() => {
                    setStatus(false)
                    NativeAd.requestAd({
                        unitId,
                    })
                }}
            >
                <Text style={styles.label}>initAd</Text>
            </Pressable>

            <Pressable style={[styles.button]}
                onPress={() => {
                    if (NativeAd.isReady(unitId)) {
                        setStatus(true);
                    }
                }}
            >
                <Text style={styles.label}>isReady</Text>
            </Pressable>

            {status && onAdLoaded ? renderItem() : null}

            {renderItem()}


            {/* {status ? (


            ): null} */}

            {/* <Pressable onPress={() => { 
                Module.requestAd({
                    unitId: '67DCFDD50DAA6C72176859126D6A3A84'
                })
             }} style={[styles.button]}>
                <Text style={[styles.label]}>requestAd</Text>
            </Pressable>

            <Pressable onPress={() => { 
                Module.loadAd("67DCFDD50DAA6C72176859126D6A3A84")
             }} style={[styles.button]}>
                <Text style={[styles.label]}>loadAd</Text>
            </Pressable>

            <Pressable onPress={() => { 
                console.info(Module.isReady('67DCFDD50DAA6C72176859126D6A3A84'))
             }} style={[styles.button]}>
                <Text style={[styles.label]}>isReady</Text>
            </Pressable>

            <Pressable onPress={() => { 
                console.info(Module.assets('67DCFDD50DAA6C72176859126D6A3A84'))
             }} style={[styles.button]}>
                <Text style={[styles.label]}>assets</Text>
            </Pressable>


            <NativeAd
                ref={nativeRef}
                request={{
                    unitId: '67DCFDD50DAA6C72176859126D6A3A84',
                    entryAdScenario: 'khalis',
                    customParams: {
                        'Admob_Adchoices': 1
                    }
                }}
                onAdLoaded={(event) => {
                    console.info(event.nativeEvent)
                }}
                onValidator={(event) => {
                    console.info(event.nativeEvent)
                    setStatus(event.nativeEvent.status)
                }}
            >
                <View style={{ margin: 5, gap: 10 }}>
                    <NativeMedia
                        source={{
                            uri: 'https://s4.ihlv1.xyz/images/20200825/5f4494c81afa9_5f4494cb3d291.jpg',
                        }}
                        style={{
                            height: 200,
                            width: '100%'
                        }}
                    />
                    <NativeChoice
                        style={{
                            height: 32,
                            width: 32,
                            position: 'absolute',
                            right: 5,
                            top: 5,

                        }}
                    />
                    <NativeIcon style={{
                        height: 50,
                        width: 50,
                    }} />
                    <NativeTitle>ads title</NativeTitle>
                    <NativeSubtitle>ads subtitle</NativeSubtitle>
                    <NativeButton
                        style={{
                            height: 50,
                            backgroundColor: '#000',
                            color: '#fff',
                            fontSize: 18,
                            textAlign: 'center',
                            textAlignVertical: 'center',
                            textTransform: 'uppercase',
                            borderRadius: 5,
                        }}
                    >Button</NativeButton>

                    <NativeChoiceIcon
                        style={{ height: 50, width: 50, backgroundColor: 'rgba(0,0,0,.2)' }}
                    />
                </View>
            </NativeAd>

            <Pressable style={[styles.button]} onPress={() => {
                nativeRef.current?.check()
            }}>
                <Text style={[styles.label]}>check</Text>
            </Pressable>


            {status ? (
                <Pressable onPress={() => { nativeRef.current?.requestAd() }} style={[styles.button]}>
                    <Text style={[styles.label]}>requestAd</Text>
                </Pressable>
            ) : null}

            {status ? (
                <Pressable onPress={() => { nativeRef.current?.showAd() }} style={[styles.button]}>
                    <Text style={[styles.label]}>showAd</Text>
                </Pressable>
            ) : null} */}
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        gap: 10,
        borderWidth: 1,
        borderColor: 'black',
        padding: 5,
        marginTop: 5,
    },
    title: {
        fontSize: 16,
        fontWeight: '800',
        textAlign: 'center',
        textTransform: 'uppercase'
    },
    button: {
        height: 50,
        width: '100%',
        backgroundColor: 'black',
        borderRadius: 5,
        alignItems: 'center',
        justifyContent: 'center'
    },
    label: {
        color: 'white',
        fontSize: 16,
        fontWeight: 'bold',
        textTransform: 'uppercase'
    },
    nativeHeaders: {
        flexDirection: 'row',
        gap: 5,
    },
    nativeTitle: {
        fontSize: 16,
        fontWeight: '600',
        lineHeight: 16
    },
    nativeMedia: {
        height: 250,
        width: '100%',
    },
    nativeChoice: {
        position: 'absolute',
        height: 32,
        width: 32,
        top: 5,
        right: 5,
    },
    nativeIcon: {
        height: 80,
        width: 80,
    },
    nativeButton: {
        height: 50,
        backgroundColor: '#000',
        color: '#fff',
        fontSize: 18,
        textAlign: 'center',
        textAlignVertical: 'center',
        textTransform: 'uppercase',
        borderRadius: 5,
    }
})

export default Native