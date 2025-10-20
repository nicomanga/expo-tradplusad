import { Dimensions, StyleSheet, Text, View } from "react-native"
import { BannerView } from 'expo-tradplusad'
import { useState } from "react";

const { width } = Dimensions.get('screen')

const AdView: React.ComponentType<{ unitId: string }> = ({
    unitId
}) => {


    return (
        <View style={[styles.box]}>
            <BannerView
                placementId={unitId}
                style={[styles.ad]}
                onAdLoaded={(event) => {
                    console.info(event.nativeEvent)
                }}
                onBannerRefreshed={(event) => {
                    console.info(event.nativeEvent);
                }}
            />
        </View>
    )
}

const BannerAd = () => {

    return (
        <View style={{ gap: 5 }}>
            <Text>BannerAd</Text>
            <AdView unitId="E538CA5BEB7AE805885B977F5EDDAEB2" />
            <View style={styles.mark} />
            <AdView unitId="1CAD6EDD1FEF16117993CE1A0104471B" />
            <View style={styles.mark} />
        </View>
    )
}

const styles = StyleSheet.create({
    mark: {
        height: 1,
        width: '100%',
        backgroundColor: 'black'
    },
    box: {
        height: width,
        width: '100%',
        alignItems: 'flex-start'
    },
    ad: {
       flex: 1,
       width: width,
       overflow: "hidden",
       backgroundColor: '#000'       
    }
})

export default BannerAd