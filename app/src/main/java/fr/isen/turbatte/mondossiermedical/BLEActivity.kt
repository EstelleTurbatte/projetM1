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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.turbatte.androidtoolbox.BLEScanAdapter
import kotlinx.android.synthetic.main.activity_bluetooth.*

class BLEActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private lateinit var handler: Handler
    private lateinit var adapter: BLEScanAdapter

    private var TAG:String = "MyActivity";


    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        BLEprogressBar.visibility = View.GONE
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

        adapter = BLEScanAdapter(arrayListOf(), ::onDeviceClicked)
        BLERecyclerView.adapter = adapter
        BLERecyclerView.layoutManager = LinearLayoutManager(this)

        playPauseImageView.setOnClickListener{scanLeDevice(true)}
    }

    private fun scanLeDevice(enable: Boolean) {
        bluetoothAdapter?.bluetoothLeScanner?.apply {

            when {
                enable -> {
                    handler.postDelayed({
                        mScanning = false
                        stopScan(leScanCallBack)
                        playPauseImageView.setImageResource(R.drawable.ic_play_arrow_24dp)
                        BLEprogressBar.visibility = View.GONE
                    }, SCAN_PERIOD)
                    mScanning = true
                    startScan(leScanCallBack)
                    playPauseImageView.setImageResource(R.drawable.ic_pause_24dp)
                    BLEprogressBar.visibility = View.VISIBLE
                }
                else -> {
                    mScanning = false
                    stopScan(leScanCallBack)
                    playPauseImageView.setImageResource(R.drawable.ic_play_arrow_24dp)
                    BLEprogressBar.visibility = View.GONE
                }
            }
        }
    }

    private val leScanCallBack = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.w("BLEActivity", "${result.device}")
            runOnUiThread {
                adapter.addDeviceToList(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isBLEEnabled) {
            scanLeDevice(false)
            playPauseImageView.setImageResource(R.drawable.ic_play_arrow_24dp)
            BLEprogressBar.visibility = View.GONE
        }
    }

    private fun onDeviceClicked(device: BluetoothDevice) {
        Toast.makeText(this@BLEActivity, "Appareil Connecté", Toast.LENGTH_LONG).show()
        connectToDevice(device)
        val intent = Intent(this, PatientEspaceActivity::class.java)
        intent.putExtra("ble_device", device)
        startActivity(intent)
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                if(newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.i(TAG, "Connected to GATT server.")
                    Toast.makeText(this@BLEActivity, "Appareil Connecté 2", Toast.LENGTH_LONG).show()

                    gatt?.discoverServices()
                }else {
                    Log.i(TAG, "Disonnected to GATT server.")
                    Toast.makeText(this@BLEActivity, "Appareil Non Connecté ", Toast.LENGTH_LONG).show()

                }
            }
            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                runOnUiThread {
                    Log.i(TAG, "Service Discovered")
                    /*bleDeviceRecyclerView.adapter = BLEServiceAdapter(
                        gatt?.services?.map {
                            BLEService(
                                it.uuid.toString(),
                                it.characteristics
                            )
                        }?.toMutableList() ?: arrayListOf(), this@BLEDeviceActivity, gatt

                    )*/
                    //bleDeviceRecyclerView.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                }

            }
        })
        bluetoothGatt?.connect()
    }
    private fun connectToCharacteristic(){

    }


    companion object {  private const val REQUEST_ENABLE_BT = 89
        private const val STATE_CONNECTED = "Connected"
        private const val SCAN_PERIOD: Long = 10000
    }
}


