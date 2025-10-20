import { Pressable, StyleSheet, Text, ToastAndroid, View } from "react-native"
import { Consent as ump, DebugGeography } from 'expo-tradplusad'

const Consent = () => {
    return (
        <View style={styles.container}>
            <Text style={[styles.title]}>Consent</Text>
            <Pressable style={[styles.button]} onPress={async() => {
                ump.requestInfoUpdate({
                    // testDeviceIdentifiers: ['31B610543A4A0CFCEFA650ECD6E40A01'],
                    // debugGeography: DebugGeography.DEBUG_GEOGRAPHY_EEA
                }).then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>requestInfoUpdate</Text>
            </Pressable>

            <Pressable style={[styles.button]} onPress={async() => {
                ump.showForm().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>showForm</Text>
            </Pressable>

            <Pressable style={[styles.button]} onPress={async() => {
                ump.showPrivacyOptionsForm().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>showPrivacyOptionsForm</Text>
            </Pressable>

            <Pressable style={[styles.button]} onPress={async() => {
                ump.loadAndShowConsentFormIfRequired().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>loadAndShowConsentFormIfRequired</Text>
            </Pressable>
            
            <Pressable style={[styles.button]} onPress={async() => {
                ump.getConsentInfo().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>getConsentInfo</Text>
            </Pressable>

            <Pressable style={[styles.button]} onPress={async() => {
                ump.reset()
            }}>
                <Text style={[styles.label]}>reset</Text>
            </Pressable>

                        <Pressable style={[styles.button]} onPress={async() => {
                ump.getTCString().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>getTCString</Text>
            </Pressable>

                        <Pressable style={[styles.button]} onPress={async() => {
                ump.getPurposeConsents().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>getPurposeConsents</Text>
            </Pressable>

                        <Pressable style={[styles.button]} onPress={async() => {
                ump.getPurposeLegitimateInterests().then(console.info).catch(console.info)
            }}>
                <Text style={[styles.label]}>getPurposeLegitimateInterests</Text>
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

export default Consent