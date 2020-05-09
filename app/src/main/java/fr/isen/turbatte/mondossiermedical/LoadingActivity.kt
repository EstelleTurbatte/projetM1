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

        /*

String s = "maString";
int n = 6; // nbre de caractères
int length = s.length();
String result = s.substring(length -n, length);

         */

        if(size_json>20){
            trame1 += size_json
            trame2 += json.subSequence(0,19)
            trame2 += json.subSequence(20,38)
            trame2 += json.subSequence(39,57)
            trame2 += json.subSequence(58,76)
            trame2 += json.subSequence(77,95)
            trame2 += json.subSequence(96,114)
            trame2 += json.subSequence(115,133)
            trame2 += json.subSequence(132,152)
            trame2 += json.subSequence(153,171)
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
                if (result.device.name == "Est" || result.device.name == "Steph")
                {
                    connectToDevice(result.device)
                    loadingTextView.text = connexion
                }
            }
        }
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        Log.i(TAG, "test")
        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    runOnUiThread {
                        loadingTextView.text = STATE_CONNECTED
                    }
                    gatt?.discoverServices()
                } else {
                    loadingTextView.text = STATE_DISCONNECTED
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, status)
                runOnUiThread {
                    //bleDeviceRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicWrite(gatt, characteristic, status)
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
                runOnUiThread {
                    Log.i(
                        TAG,
                        "Services découverts :  $serviceList "
                    )
                    if (characteristic != null) {
                        gatt?.writeCharacteristic(characteristic)
                        characteristic?.value = text.toByteArray()
                        gatt?.readCharacteristic(characteristic)
                        val reception = characteristic?.value?.let { String(it) }
                        if (reception != null) {
                            val valeurRecue = "valeur recue : $reception"
                            Log.i(TAG, "VALEUR : $valeurRecue")
                        }
                    }
                }

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
