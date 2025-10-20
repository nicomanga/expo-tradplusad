import { Pressable, StyleSheet, Text, ToastAndroid, View } from "react-native"
import { RewardAd as Reward } from 'expo-tradplusad'

const RewardAd = () => {
    return (
        <View style={styles.container}>
            <Text style={[styles.title]}>RewardAd</Text>
            <Pressable style={[styles.button]} onPress={() => {
                Reward.initAd('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show(`Reward Start`, 500)
            }}>
                <Text style={[styles.label]}>instalization</Text>
            </Pressable>

            <Pressable onPress={() => {
                Reward.loadAd('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show(`Reward Load`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>LoadAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                ToastAndroid.show(`Reward ${Reward.isReady('7F5B1236C411E12AF721E669A0B7A4E3')}`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>isReady</Text>
            </Pressable>

            <Pressable onPress={() => {
                Reward.showAd('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show("Reward", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>showAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                Reward.reloadAd('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show(`Reward Reload`, 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>reloadAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                Reward.clearCacheAd('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show("Reward clearCacheAd", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>clearCacheAd</Text>
            </Pressable>

            <Pressable onPress={() => {
                Reward.entryAdScenario('7F5B1236C411E12AF721E669A0B7A4E3', "tp-test")
                ToastAndroid.show("Reward entryAdScenario tp-test", 500)
            }} style={[styles.button]}>
                <Text style={[styles.label]}>entryAdScenario</Text>
            </Pressable>

            <Pressable onPress={() => {
                Reward.onDestroy('7F5B1236C411E12AF721E669A0B7A4E3')
                ToastAndroid.show("Reward onDestroy", 500)
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

export default RewardAd