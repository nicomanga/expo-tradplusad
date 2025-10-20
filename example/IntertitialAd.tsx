import { Pressable, StyleSheet, Text, ToastAndroid, View } from "react-native"
import Intertitial from 'expo-tradplusad/ExpoIntertitialModule'

const IntertitialAd = () => {
    return (
        <View style={styles.container}>
            <Text style={[styles.title]}>RewardAd</Text>
            <Pressable style={[styles.button]} onPress={() => {
                Intertitial.initAd('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show(`Intertitial Start`, 500)
            }}>
                <Text style={[styles.label]}>instalization</Text>
            </Pressable>

            <Pressable onPress={() => {
                Intertitial.loadAd()
                ToastAndroid.show(`Intertitial Load`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>LoadAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                ToastAndroid.show(`Intertitial ${Intertitial.isReady()}`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>LoadAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                Intertitial.showAd()
                ToastAndroid.show("Intertitial", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>LoadAd</Text>
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

export default IntertitialAd