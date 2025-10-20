import { Pressable, StyleSheet, Text, ToastAndroid, View } from "react-native"
import { Splash } from 'expo-tradplusad'
import { useEvent } from "expo"

const SplashAd = () => {

    const onFailed = useEvent(Splash, 'onAdLoaded')

    console.info(onFailed);


    return (
        <View style={styles.container}>
            <Text style={[styles.title]}>SplashAd</Text>
            <Pressable style={[styles.button]} onPress={() => {
                Splash.initAd("125315F95F59A47F89A68C805539A312")
                ToastAndroid.show(`Splash Start`, 500)
            }}>
                <Text style={[styles.label]}>instalization</Text>
            </Pressable>

            <Pressable onPress={() => {
                Splash.loadAd()
                ToastAndroid.show(`Splash Load`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>LoadAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                ToastAndroid.show(`Splash ${Splash.isReady()}`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>isReady</Text>
            </Pressable>

            <Pressable onPress={() => {
                Splash.showAd()
                ToastAndroid.show("Splash", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>showAd</Text>
            </Pressable>


            <Pressable onPress={() => {
                Splash.onClean()
                ToastAndroid.show("Splash onClean", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>onClean</Text>
            </Pressable>

            <Pressable onPress={() => {
                Splash.entryAdScenario("tp-test")
                ToastAndroid.show("Splash entryAdScenario tp-test", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>entryAdScenario</Text>
            </Pressable>

            <Pressable onPress={() => {
                Splash.onDestroy()
                ToastAndroid.show("Splash onDestroy", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>onDestroy</Text>
            </Pressable>
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
    }
})

export default SplashAd