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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_modifier_count_patient.*
import org.json.JSONObject
import java.util.*

class ModifierCountPatientActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "MyActivity"


    private val COMMANDE = 10

    private var messageAEnvoyer = ""

    private lateinit var handler: Handler
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modifier_count_patient)



        //val intent = Intent(this, ModifierCountPatientActivity::class.java)

        var precedentNom:String = intent.getStringExtra("USERNOM")
        var precedentPrenom:String = intent.getStringExtra("USERPRENOM")
        var precedentNumSecu:String = intent.getStringExtra("USERNUMSECU")
        var precedentMail:String = intent.getStringExtra("USERMAIL")
        var precedentTel:String = intent.getStringExtra("USERTEL")
        var precedentAdress:String = intent.getStringExtra("USERADRESS")
        var precedentMedecin:String = intent.getStringExtra("USERMEDECIN")

        Log.i(TAG, "1:$precedentNom")

        envoyerModificationPatientButton.setOnClickListener {

            val userNom:String = userNomText.text.toString()
            val userPrenom:String = userPrenomText.text.toString()
            //date de naissance à gérer
            val userNumeroSecu:String = userNumeroSecuText.text.toString()
            val userEmail:String = userEMailText.text.toString()
            val userTelephone:String = userTelephoneText.text.toString()
            val userAdress:String = userAdressText.text.toString()
            val userMedecinTraitant:String = userMedecinText.text.toString()

            //Log.i(TAG, "1:$precedentNom")

            if (userNom != precedentNom && userNom != ""){
                precedentNom = userNom
               // Log.i(TAG, "2:$precedentNom")
            }
            if (userPrenom != precedentPrenom && userPrenom != ""){
                precedentPrenom = userPrenom
            }
            if (userNumeroSecu != precedentNumSecu && userNumeroSecu != ""){
                precedentNumSecu = userNumeroSecu
            }
            if (userEmail != precedentMail && userEmail != ""){
                precedentMail = userEmail
            }
            if (userTelephone != precedentTel && userTelephone != ""){
                precedentTel = userTelephone
            }
            if(userAdress != precedentAdress && userAdress != ""){
                precedentAdress = userAdress
            }
            if(userMedecinTraitant != precedentMedecin && userMedecinTraitant != ""){
                precedentMedecin = userMedecinTraitant
            }

            val JSONObj = JSONObject()

            JSONObj.put("Commande", COMMANDE)
            JSONObj.put("Nom", precedentNom)
            JSONObj.put("Prenom", precedentPrenom)
            JSONObj.put("NumeroSecu", precedentNumSecu)
            JSONObj.put("Email", precedentMail)
            JSONObj.put("Adresse", precedentAdress)
            JSONObj.put("Telephone", precedentTel)
            JSONObj.put("MedecinTraitant", precedentMedecin)
            JSONObj.put("Age", "89")

            val json = JSONObj.toString()

            messageAEnvoyer =
                "{\"Commande\":$COMMANDE,\"Nom\":$precedentNom,\"Prenom\":$precedentPrenom,\"NumeSecu:$precedentNumSecu,\"Mail:$precedentMail,\"Adresse:$precedentAdress,\"Tel:$precedentTel,\"Medecin:$precedentMedecin;age}"

            //"{\"Commande\":3,\"Id\":0}"

            //Log.i(TAG, messageAEnvoyer)
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("MESSAGE", messageAEnvoyer)
            intent.putExtra("JSON_BLE", json)

            startActivity(intent)
        }
    }

}
