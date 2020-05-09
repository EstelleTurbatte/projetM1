package fr.isen.turbatte.mondossiermedical

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_ordonnance.*
import org.json.JSONObject
import java.time.LocalDateTime


class NewOrdonnanceActivity : AppCompatActivity() {

    val KEY_DATE = "Date"
    val KEY_INFO_MEDECIN = "Medecin"
    val KEY_MOTIF = "Motif"
    val KEY_PRESCRIPTION = "Medicament"
    val commande = 7
    val COMMANDE = "Commande"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ordonnance)

        val id = intent.getStringExtra("id")

        val date = LocalDateTime.now()
        val medecin = infoDocTextView.text.toString()
        val motif = motifTextView.text.toString()
        val prescription = prescriptionTextView.text.toString()

        dateTextView.text = date.toString()

        val JSONObj = JSONObject()

        JSONObj.put(COMMANDE, commande)
        JSONObj.put(KEY_DATE, date)
        JSONObj.put(KEY_INFO_MEDECIN, medecin)
        JSONObj.put(KEY_MOTIF, motif)
        JSONObj.put(KEY_PRESCRIPTION, prescription)
        JSONObj.put("Id", id)

        val json = JSONObj.toString()

        envoyerButton.setOnClickListener {
          val intent = Intent(this, NFCActivity::class.java)
            intent.putExtra("JSON",json)
            startActivity(intent)
        }
    }
}
