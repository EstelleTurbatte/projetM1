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
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlinx.android.synthetic.main.activity_loading.*
import java.util.*
import java.util.UUID.fromString

class LoadingActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "BLE_ACTIVITY"
    private var messageAEnvoyer:String = ""
    private val intent2 = Intent(this, CountPatientActivity::class.java)
    private var json = ""

    private var trame1 = "1020 "
    private var trame2 = "1"
    private var trame3 = "2"
    private var trame4 = "3"
    private var trame5 = "4"
    private var trame6 = "5"
    private var trame7 = "6"
    private var trame8 = "7"
    private var trame9 = "8"
    private var trame10 = "9"

    private var autorisation:Boolean = false

    private var numAutorisation:Int = 0

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

        json = intent.getStringExtra("JSON_BLE")
        val size_json = json.length
        Log.i(TAG, size_json.toString())



        if(size_json>20){
            trame1 += size_json
            trame2 += json.subSequence(0,19)
            numAutorisation = 1
            if(size_json>39){
                trame3 += json.subSequence(20,38)
                numAutorisation = 2
                if(size_json>58){
                    trame4 += json.subSequence(39,57)
                    numAutorisation = 3
                    if(size_json>77){
                        trame5 += json.subSequence(58,76)
                        numAutorisation = 4
                        if(size_json>96){
                            trame6 += json.subSequence(77,95)
                            numAutorisation = 5
                            if(size_json>115){
                                trame7 += json.subSequence(96,114)
                                numAutorisation = 6
                                if(size_json>134){
                                    trame8 += json.subSequence(115,133)
                                    numAutorisation = 7
                                    if(size_json>153){
                                        trame9 += json.subSequence(132,152)
                                        numAutorisation = 8
                                        if(size_json>172){
                                            trame10 += json.subSequence(153,171)
                                            numAutorisation = 9
                                        }else{
                                            trame10 += json.subSequence(153,size_json)
                                        }
                                    }else{
                                        trame9 += json.subSequence(132,size_json)
                                    }
                                }else{
                                    trame8 += json.subSequence(115,size_json)
                                }
                            }else{
                                trame7 += json.subSequence(96,size_json)
                            }
                        }else{
                            trame6 += json.subSequence(77,size_json)
                        }
                    }else{
                        trame5 += json.subSequence(58,size_json)
                    }
                }else{
                    trame4 += json.subSequence(39,size_json)
                }
            }else{
                trame3 += json.subSequence(20,size_json)
            }
        }else{
            trame1 += size_json
            trame2 += json.subSequence(0,size_json)
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
        val intent = Intent(this, CountPatientActivity::class.java)
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
                characteristic?.setValue(trame1.toByteArray())
                gatt?.writeCharacteristic(characteristic)
                //Log.i(TAG, " ici : 2")
                characteristic?.setValue(trame2.toByteArray())
                gatt?.writeCharacteristic(characteristic)
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

                characteristic?.setValue(text.toByteArray())
                gatt?.writeCharacteristic(characteristic)

                /*when(numAutorisation) {
                    1 -> runOnUiThread {
                        Log.i(TAG," 1 " )
                        if (characteristic != null) {

                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            gatt?.writeCharacteristic(characteristic)

                            /*gatt?.readCharacteristic(characteristic)
                            val reception = characteristic?.value?.let { String(it) }
                            if (reception != null) {
                                val valeurRecue = "valeur recue : $reception"
                                Log.i(TAG, "VALEUR : $valeurRecue")
                            }*/
                        }
                    }
                    2 -> runOnUiThread {
                        Log.i(
                            TAG,
                            " 2 "
                        )
                        if (characteristic != null) {

                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                        }
                    }
                    3 -> runOnUiThread {
                        Log.i(
                            TAG,
                            " 3 "
                        )
                        if (characteristic != null) {

                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            characteristic?.setValue(trame4.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                        }
                    }
                    4 -> runOnUiThread {
                        Log.i(
                            TAG,
                            " 4 "
                        )
                        if (characteristic != null) {

                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            characteristic?.setValue(trame4.toByteArray())
                            characteristic?.setValue(trame5.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                        }
                    }
                    5 -> runOnUiThread {
                        Log.i(
                            TAG,
                            " 5 "
                        )
                        if (characteristic != null) {

                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            characteristic?.setValue(trame4.toByteArray())
                            characteristic?.setValue(trame5.toByteArray())
                            characteristic?.setValue(trame6.toByteArray())
                            gatt?.writeCharacteristic(characteristic)

                        }
                    }
                    6 -> runOnUiThread {
                        Log.i(TAG," 6 ")
                        if (characteristic != null) {

                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            characteristic?.setValue(trame4.toByteArray())
                            characteristic?.setValue(trame5.toByteArray())
                            characteristic?.setValue(trame6.toByteArray())
                            characteristic?.setValue(trame7.toByteArray())
                            gatt?.writeCharacteristic(characteristic)

                        }
                    }
                    7 -> runOnUiThread {
                        Log.i(TAG, " 7 ")
                        if (characteristic != null) {
                            //Log.i(TAG, " ici : 1")
                            characteristic?.setValue(json.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 2")
                            characteristic?.setValue(trame2.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 3")
                            characteristic?.setValue(trame3.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 4")
                            characteristic?.setValue(trame4.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 5")
                            characteristic?.setValue(trame5.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 6")
                            characteristic?.setValue(trame6.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 7")
                            characteristic?.setValue(trame7.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 8")
                            characteristic?.setValue(trame8.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                            //Log.i(TAG, " ici : 9")
                        }
                    }
                    8 -> runOnUiThread {
                        Log.i(TAG," 8 ")
                        if (characteristic != null) {
                            Log.i(TAG, " ici8 ")
                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            characteristic?.setValue(trame4.toByteArray())
                            characteristic?.setValue(trame5.toByteArray())
                            characteristic?.setValue(trame6.toByteArray())
                            characteristic?.setValue(trame7.toByteArray())
                            characteristic?.setValue(trame8.toByteArray())
                            characteristic?.setValue(trame9.toByteArray())
                            gatt?.writeCharacteristic(characteristic)
                        }
                    }
                    9 -> runOnUiThread {
                        Log.i( TAG," 9 " )
                        if (characteristic != null) {
                            Log.i(TAG, " ici9 ")
                            characteristic?.setValue(trame1.toByteArray())
                            characteristic?.setValue(trame2.toByteArray())
                            characteristic?.setValue(trame3.toByteArray())
                            characteristic?.setValue(trame4.toByteArray())
                            characteristic?.setValue(trame5.toByteArray())
                            characteristic?.setValue(trame6.toByteArray())
                            characteristic?.setValue(trame7.toByteArray())
                            characteristic?.setValue(trame8.toByteArray())
                            characteristic?.setValue(trame9.toByteArray())
                            characteristic?.setValue(trame10.toByteArray())
                            gatt?.writeCharacteristic(characteristic)

                        }
                    }


                }*/

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
