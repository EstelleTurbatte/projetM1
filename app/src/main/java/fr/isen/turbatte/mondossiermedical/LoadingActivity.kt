package fr.isen.turbatte.mondossiermedical

import android.bluetooth.*
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlinx.android.synthetic.main.activity_loading.*
import kotlinx.coroutines.delay
import java.util.*
import java.util.UUID.fromString

class LoadingActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "BLE_ACTIVITY"
    private var messageAEnvoyer:String = ""
    private val intent2 = Intent(this, CountPatientActivity::class.java)
    private var json2 = ""
    private var json = ""


    private var message = "1020 "

    //BSO"
    private var listTrame:MutableList<String> = mutableListOf()

    private var autorisation:Boolean = false

    private var numAutorisation:Int = 0
    private var declenchement :Int = 0

    private lateinit var handler: Handler
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        json2 = intent.getStringExtra("JSON_BLE")

        val size_json = json2.length

        message += "$size_json           "
        json = message + json2
       // message += json
        Log.i(TAG, size_json.toString())

        val size_message: Int = json.length
        Log.i(TAG, "taille message : $size_message.toString()")
            //BSO
        if(size_message>20){
          // listTrame.add(size_message.toString())
            listTrame.add(json.subSequence(0,19).toString())

            numAutorisation = 1
            if(size_message>39){
                listTrame.add(json.subSequence(19,38).toString())
                numAutorisation = 2
                if(size_message>58){
                    listTrame.add(json.subSequence(38,57).toString())
                    numAutorisation = 3
                    if(size_message>77){
                        listTrame.add(json.subSequence(57,76).toString())
                        numAutorisation = 4
                        if(size_message>96){
                            listTrame.add(json.subSequence(76,95).toString())
                            numAutorisation = 5
                            if(size_message>115){
                                listTrame.add(json.subSequence(95,114).toString())
                                numAutorisation = 6
                                if(size_message>134){
                                    listTrame.add(json.subSequence(114,133).toString())
                                    numAutorisation = 7
                                    if(size_message>153){
                                        listTrame.add(json.subSequence(133,152).toString())
                                        numAutorisation = 8
                                        if(size_message>172){
                                            listTrame.add(json.subSequence(152,171).toString())
                                            numAutorisation = 9
                                            if(size_message>191){
                                                listTrame.add(json.subSequence(171,190).toString())
                                                if(size_message>210){
                                                    listTrame.add(json.subSequence(190,209).toString())
                                                    if(size_message>229){
                                                        listTrame.add(json.subSequence(209,228).toString())
                                                        if(size_message>248){
                                                            listTrame.add(json.subSequence(228,247).toString())
                                                            if(size_message>267){
                                                                listTrame.add(json.subSequence(247,266).toString())
                                                            }else{
                                                                listTrame.add(json.subSequence(247,size_message).toString())
                                                            }
                                                        }else{
                                                            listTrame.add(json.subSequence(228,size_message).toString())
                                                        }
                                                    }else{
                                                        listTrame.add(json.subSequence(209,size_message).toString())
                                                    }
                                                }else{
                                                    listTrame.add(json.subSequence(190,size_message).toString())
                                                }
                                            }else{
                                                listTrame.add(json.subSequence(171,size_message).toString())
                                            }
                                        }else{
                                            listTrame.add(json.subSequence(152,size_message).toString())
                                        }
                                    }else{
                                        listTrame.add(json.subSequence(133,size_message).toString())
                                    }
                                }else{
                                    listTrame.add(json.subSequence(114,size_message).toString())
                                }
                            }else{
                                listTrame.add(json.subSequence(95,size_message).toString())
                            }
                        }else{
                            listTrame.add(json.subSequence(76,size_message).toString())
                        }
                    }else{
                        listTrame.add(json.subSequence(57,size_message).toString())
                    }
                }else{
                    listTrame.add(json.subSequence(38,size_message).toString())
                }
            }else{
                listTrame.add(json.subSequence(19,size_message).toString())
            }
        }else{
            //listTrame.add(size_json.toString())
            listTrame.add(json.subSequence(0,size_message).toString())
        }

        messageAEnvoyer = intent.getStringExtra("MESSAGE")

        when {
            isBLEEnabled -> {
                initScan()
            }
            bluetoothAdapter != null -> {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
            }
            else -> {
                blefailedText.visibility = View.VISIBLE
            }
        }
    }
    private fun initScan() {
        handler = Handler()
        scanLeDevice(true)
    }

    private fun scanLeDevice(enable: Boolean) {
        val intent = Intent(this, PatientEspaceActivity::class.java)
        bluetoothAdapter?.bluetoothLeScanner?.apply {
            when {
                enable -> {
                    handler.postDelayed({
                        mScanning = false
                        stopScan(leScanCallBack)
                        loadingTextView.text = arretScan
                        startActivity(intent)
                    }, SCAN_PERIOD)
                    mScanning = true
                    startScan(leScanCallBack)
                    loadingTextView.text = scanEnCours
                }
                else -> {
                    mScanning = false
                    stopScan(leScanCallBack)
                    loadingTextView.text = arretScan
                    startActivity(intent)
                }
            }
        }
    }

    private val leScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLEActivity", "${result.device}")
            runOnUiThread {
                /*
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()*/
                if (result.device.name == "Est" || result.device.name == "Step")
                {
                    connectToDevice(result.device)
                    loadingTextView.text = connexion
                }
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    runOnUiThread {
                        loadingTextView.text = STATE_CONNECTED
                    }
                    gatt?.discoverServices()
                } else {
                    runOnUiThread {
                        loadingTextView.text = STATE_DISCONNECTED
                    }
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
                runOnUiThread {
                    loadingTextView.text = "caractéristique :${characteristic?.uuid}"
                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicWrite(gatt, characteristic, status)

                if(status == GATT_SUCCESS){
                    //On check si la liste n'est pas vide
                    if(listTrame.isNotEmpty()){
                        //Si la liste n'est pas vide alors on envoie la prochaine trame de la liste
                        Log.i(TAG, listTrame.get(0))
                        characteristic?.setValue(listTrame.get(0))
                        gatt?.writeCharacteristic(characteristic)
                        //On supprime la trame de la liste pour le reste du traitement
                        listTrame.removeAt(0)

                    }

                    //Avec ce code, dès que la liste est vide on envoi plus rien
                    //Par contre c'est à la Nordic de détecter dès que des données sont écrites dans la charactéristique
                    //Afin de les sauvegarder au fur et à mesure car les anciennes données seront écrasées
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                Log.i(
                    TAG,
                    "**THIS IS A NOTIFY MESSAGE" + characteristic?.value + characteristic?.uuid
                )
                runOnUiThread {
                    //bleDeviceRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                val serviceList = gatt?.services
                val characteristic:BluetoothGattCharacteristic? = gatt?.getService(UUIDService)?.getCharacteristic(
                    UUIDCharac)
                val text:String = json
                val text2:String = "coucou"

                //BSO
                Log.i(TAG, " ici : 1")
                //On envoi une seule trame contenue dans la liste
                characteristic?.setValue(listTrame.get(0))
                gatt?.writeCharacteristic(characteristic)
                //On supprime la trame de la liste pour le reste du traitement
                listTrame.removeAt(0)

            }
        })
        bluetoothGatt?.connect()
    }

    private fun connectToService(){

    }

    override fun onPause() {
        super.onPause()
        if (isBLEEnabled) {
            scanLeDevice(false)
            loadingTextView.text = arretScan
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
        private var UUIDService: UUID = fromString("466c1234-f593-11e8-8eb2-f2801f1b9fd1")
        private var UUIDCharac: UUID = fromString("466c9abc-f593-11e8-8eb2-f2801f1b9fd1")
    }
}
