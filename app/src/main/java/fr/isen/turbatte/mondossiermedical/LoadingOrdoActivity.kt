package fr.isen.turbatte.mondossiermedical

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import fr.isen.turbatte.mondossiermedical.Utils.Companion.byteArrayToHexString
import kotlinx.android.synthetic.main.activity_loading_ordo.*
import org.json.JSONObject
import java.util.*

class LoadingOrdoActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "BLE_ACTIVITY"
    private var messageAEnvoyer:String = ""
    private var json2 = ""
    private var json = ""
    private var notity: Boolean = false
    private var listTrame:MutableList<String> = mutableListOf()
    private var listTrame2:MutableList<String> = mutableListOf()
    private var message = "1020 "
    private var messageRecu = "test"
    private var reception =""
    //private val intent2 = Intent(this, CountPatientActivity::class.java)


    private lateinit var handler: Handler
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_ordo)

        val JSONObj = JSONObject()

        JSONObj.put("Commande", 0)

        Log.i("BLE_CHARGEMENT", "On create")
        json = JSONObj.toString()


        val size_json = json.length
        message += "$size_json            "
        //Log.i(TAG, "MESSAGE : $message")
        json = message + json
        //Log.i(TAG, "JSON : $json")
        val size_message: Int = json.length
        //Log.i(TAG, "size_message : $size_message")

        if(size_message>20) {
            listTrame.add(json.subSequence(0, 19).toString())
            if(size_message>39) {
                //listTrame.add(size_message.toString())
                listTrame.add(json.subSequence(19, 38).toString())
            }else{
                listTrame.add(json.subSequence(19,size_message).toString())
            }
        }else{
            listTrame.add(json.subSequence(0,size_message).toString())
        }
        when {
            isBLEEnabled -> {
                initScan()
            }
            bluetoothAdapter != null -> {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            }
            else -> {
                messageText.visibility = View.VISIBLE
            }
        }
    }
    private fun initScan() {
        //Log.i("BLE_CHARGEMENT", "init scan")
        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        //Log.i("BLE_CHARGEMENT", "scan le device")
        val intent2 = Intent(this, MesOrdonnancesActivity::class.java)
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            when {
                enable -> {
                    handler.postDelayed({
                        mScanning = false
                        stopScan(leScanCallBack)
                        messageText.text = arretScan
                        Log.i(TAG, "MESSAGE FINAL "+messageRecu)
                        intent2.putExtra("MESSAGE_BLE", messageRecu)
                        //intent2.putExtra("MESSAGE", messageAEnvoyer)
                        startActivity(intent2)
                    }, SCAN_PERIOD)
                    mScanning = true
                    //Log.i("BLE_CHARGEMENT", "true")
                    startScan(leScanCallBack)
                    messageText.text = scanEnCours
                }
                else -> {
                    mScanning = false
                    stopScan(leScanCallBack)
                    messageText.text = arretScan
                    intent2.putExtra("MESSAGE_BLE", messageRecu)
                    startActivity(intent2)
                }
            }
        }
    }

    private val leScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            //Log.w("BLEActivity", "${result.device}")
            runOnUiThread {
                /*
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()*/
                //Log.i("BLE_CHARGEMENT", result.device.name)
                if (result.device.name == "Est" || result.device.name == "Step")
                {
                    //Log.i("BLE_CHARGEMENT", "dans le if")
                    connectToDevice(result.device)
                    messageText.text = connexion
                }
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        val intent2 = Intent(this, CountPatientActivity::class.java)
        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //Log.i("BLE_CHARGEMENT", "connecté")
                    runOnUiThread {
                        messageText.text = STATE_CONNECTED
                    }
                    gatt?.discoverServices()
                } else {
                    runOnUiThread {
                        messageText.text = STATE_DISCONNECTED
                    }
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
                if (characteristic != null) {
                    runOnUiThread {
                        // loadingTextView2.text = "caractéristique :${characteristic?.uuid}"
                        /*if (listTrame.get(0) == "1020 "){
                                    gatt?.readCharacteristic(characteristic)
                                    if (characteristic != null) {
                                        listTrame.add(String(characteristic.value))
                                        Log.i(TAG, "listTrame.get(0)")
                                    }
                                }*/
                        reception += String(characteristic.value)
                        if (reception != null) {
                            val valeurRecue = "valeur recue : $reception"
                            Log.i(TAG, "RECEPTION READ : $valeurRecue")
                        }
                    }
                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicWrite(gatt, characteristic, status)

                if(status == BluetoothGatt.GATT_SUCCESS){
                    //On check si la liste n'est pas vide
                    if(listTrame.isNotEmpty()){
                        //Si la liste n'est pas vide alors on envoie la prochaine trame de la liste
                        Log.i(TAG, "WRITE ${listTrame.get(0)}")
                        characteristic?.setValue(listTrame.get(0))
                        gatt?.writeCharacteristic(characteristic)
                        //On supprime la trame de la liste pour le reste du traitement
                        listTrame.removeAt(0)

                    }else{
                        if (!notity) {
                            notity = true
                            gatt?.setCharacteristicNotification(characteristic, true)

                            if (characteristic?.descriptors?.size!! > 0) {

                                val descriptors = characteristic.descriptors
                                for (descriptor in descriptors) {
                                    if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                                    } else if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                                        descriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                                    }
                                    gatt?.writeDescriptor(descriptor)
                                }
                            }
                        } else {
                            notity = false
                            gatt?.setCharacteristicNotification(characteristic, false)
                        }
                    }
                }
            }

            override fun onDescriptorWrite(
                gatt: BluetoothGatt?,
                descriptor: BluetoothGattDescriptor?,
                status: Int
            ) {
                super.onDescriptorWrite(gatt, descriptor, status)
                if(status == BluetoothGatt.GATT_SUCCESS){
                    Log.i("TAG", "Descriptor Written ")
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                //Log.i(TAG, "Valeur 2 Hexa : ${characteristic?.value?.let { byteArrayToHexString(it) }}")
                Log.i(TAG, "Valeur 2 String : ${characteristic?.value?.let { String(it) }}")
                messageRecu += characteristic?.value?.let { String(it) }
                Log.i(TAG, "MESSAGE RECU : $messageRecu")
                //val intent2 = Intent(this, CountPatientActivity::class.java)
                //intent2.putExtra("MESSAGE", messageRecu)
                //startActivity(intent2)
                //gatt?.readCharacteristic(characteristic)

                runOnUiThread {
                    //bleDeviceRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                val serviceList = gatt?.services
                val characteristic: BluetoothGattCharacteristic? =
                    gatt?.getService(UUIDService)?.getCharacteristic(
                        UUIDCharac
                    )
                //Log.i(TAG,"**THIS IS A NOTIFY MESSAGE" + characteristic?.value + characteristic?.uuid)
                //Log.i(TAG,"JSON envoyé " + json)
                val size:Int = json.length
                json = "1020 $size"+json
                //Log.i(TAG,"JSON" + json)
                characteristic?.setValue(listTrame.get(0))
                gatt?.writeCharacteristic(characteristic)
                //On supprime la trame de la liste pour le reste du traitement
                listTrame.removeAt(0)

            }
        })
        bluetoothGatt?.connect()
    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    override fun onPause() {
        super.onPause()
        if (isBLEEnabled) {
            scanLeDevice(false)
            messageText.text = arretScan
        }
    }

    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 89
        private const val SCAN_PERIOD: Long = 10000
        private var scanEnCours:String = "Scan en cours"
        private var deviceTrouve:String = "Device Trouvé"
        private var arretScan:String = "Arrêt du Scan"
        private var connexionEnCours:String = "Connexion en cours"
        private var connexion:String = "Connexion effectuée"
        private const val STATE_DISCONNECTED = "Disconnected"
        private const val STATE_CONNECTING = "Connecting"
        private const val STATE_CONNECTED = "Connected"
        private var UUIDService: UUID = UUID.fromString("466c1234-f593-11e8-8eb2-f2801f1b9fd1")
        private var UUIDCharac: UUID = UUID.fromString("466c9abc-f593-11e8-8eb2-f2801f1b9fd1")
    }
}


