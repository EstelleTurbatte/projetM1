package fr.isen.turbatte.mondossiermedical

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_ordonnance.*
import org.json.JSONObject


class NewOrdonnanceActivity : AppCompatActivity() {

    val KEY_DATE = "DATE"
    val KEY_INFO_MEDECIN = "INFO MEDECIN"
    val KEY_MOTIF = "MOTIF"
    val KEY_PRESCRIPTION = "PRESCRIPTION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ordonnance)

        val gatt: BluetoothGatt?

        //val characteristic: BluetoothGattCharacteristic = BLEService().items

        val date = dateTextView.text.toString()
        val medecin = infoDocTextView.text.toString()
        val motif = motifTextView.text.toString()
        val prescription = prescriptionTextView.text.toString()

        val JSONObj = JSONObject()

        JSONObj.put(KEY_DATE, date)
        JSONObj.put(KEY_INFO_MEDECIN, medecin)
        JSONObj.put(KEY_MOTIF, motif)
        JSONObj.put(KEY_PRESCRIPTION, prescription)

        val json = JSONObj.toString()

        // characteristic.value = json.toString().toByteArray()


        envoyerButton.setOnClickListener {
          /*  val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)*/

        }
    }
}
