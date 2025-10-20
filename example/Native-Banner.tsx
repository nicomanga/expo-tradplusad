import { useEventListener } from "expo"
import { NativeAd, NativeAdViewMap, NativeButton, NativeChoice, NativeIcon, NativeIconChoice, NativeMediaView, NativeSubtitle, NativeTitle, NativeView, NativeViewRef } from "expo-tradplusad"
import { use, useEffect, useRef, useState } from "react"
import { Dimensions, Pressable, StyleSheet, Text, View } from "react-native"

const source = { uri: 'https://s4.ihlv1.xyz/images/20200825/5f4494c81afa9_5f4494cb3d291.jpg' }

const NativeBanner = () => {
    const ref = useRef<NativeViewRef>(null)
    const [status, setStatus] = useState(false)
    const [base, setBase] = useState<NativeAdViewMap>({})

    useEventListener(NativeAd, 'onAdLoaded', ({ tpBaseAd }) => {
        setStatus(true)
        setBase(tpBaseAd)
    })

    useEffect(() => {
        NativeAd.requestAd({
            unitId: '67DCFDD50DAA6C72176859126D6A3A84',
            size: {
                height: 300,
                width: Dimensions.get('window').width,
            }
        })
    }, [])

    const size = (init = 80) => {
        const nativeAd = base.nativeAd
        if (nativeAd?.iconView || nativeAd?.iconImageUrl || nativeAd?.iconImage) {
            return init
        }
        return 0
    }

    if (!status) return null;

    return (
        <>
            <Text>{JSON.stringify(base)}</Text>
            <NativeView
                ref={ref}
                requestAd="67DCFDD50DAA6C72176859126D6A3A84" 
                assets={base}  
                onReady={() => {
                    console.info('ready')
                }}
                onDisplay={() => {
                    console.info('display')
                }}
                >
                <View style={{ padding: 5 }}>
                    <View style={{ flexDirection: 'row', marginVertical: 5, gap: 5 }}>
                        <NativeIcon
                            source={source}
                            resizeMode="cover"
                            style={{ 
                                height: size(), 
                                width: size() 
                            }}
                        />
                        <View>
                            <NativeTitle style={styles.title}>{base.title}</NativeTitle>
                            {base.advertiserName ? <Text style={styles.title}>{base.advertiserName}</Text> : null}
                        </View>
                    </View>
                    <View>
                        <NativeMediaView
                            source={source}
                            resizeMode="cover"
                            style={{ height: 250 / (base.aspectRatio ? base.aspectRatio : 1) , width: '100%' }}
                        />
                        <NativeChoice
                            style={{
                                position: 'absolute',
                                top: 0,
                                height: 32,
                                width: 32, 
                                right: 0
                            }}
                        />
                        <NativeIconChoice
                            source={{ uri: 'https://storage.googleapis.com/support-kms-prod/B39986E0A9616888E3CB26D02451EC166BAD' }}
                            style={{
                                position: 'absolute',
                                top: 0,
                                left: 0,
                                height: 32,
                                width: 32,
                            }}
                        />
                    </View>
                    <View>
                        <NativeSubtitle>{base.subtitle}</NativeSubtitle>
                        <NativeButton style={{
                            height: 50,
                            backgroundColor: '#000',
                            color: '#fff',
                            width: '100%',
                            borderRadius: 8,
                            textAlign: 'center',
                            textAlignVertical: 'center',
                            fontSize: 20,
                            textTransform: 'uppercase'
                        }}>open</NativeButton>
                    </View>
                </View>
            </NativeView>
            <Pressable onPress={() => {
                ref.current?.check()
                ref.current?.showAd()
                console.info('show')
            }}>
                <Text style={{
                    height: 50,
                            backgroundColor: '#000',
                            color: '#fff',
                            width: '100%',
                            borderRadius: 8,
                            textAlign: 'center',
                            textAlignVertical: 'center',
                            fontSize: 20,
                            textTransform: 'uppercase'
                }}>Show</Text>
            </Pressable>
        </>
    )
}

const styles = StyleSheet.create({
    title: {
        fontSize: 16,
        fontWeight: 'bold',
        letterSpacing: 1.2,
        lineHeight: 16
    }
})

export default NativeBanner