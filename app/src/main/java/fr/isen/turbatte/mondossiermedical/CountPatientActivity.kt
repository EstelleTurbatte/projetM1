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
import com.google.gson.Gson
import fr.isen.turbatte.mondossiermedical.Utils.Companion.byteArrayToHexString
import kotlinx.android.synthetic.main.activity_count_patient.*
import org.json.JSONObject
import java.util.*

class CountPatientActivity : AppCompatActivity() {

    private var mScanning: Boolean = false
    private var bluetoothGatt: BluetoothGatt? = null
    private var TAG: String = "BLE_ACTIVITY"
    private var messageAEnvoyer:String = ""
    private val intent2 = Intent(this, CountPatientActivity::class.java)
    private var json2 = ""
    private var json = ""
    private var notity: Boolean = false
    private var listTrame:MutableList<String> = mutableListOf()
    private var messageRecu = ""


    private lateinit var handler: Handler
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val isBLEEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_patient)

        messageRecu = intent.getStringExtra("MESSAGE")

        val JSONObj = JSONObject()

        JSONObj.put("Commande", 6)

//test.get(KEY_LOGIN).toString()
        val gson = Gson()

        var MessageObject:JSONObject = gson.fromJson(messageRecu, JSONObject::class.java)

        json = JSONObj.toString()

        val Nom: String = "NOM : "
        val userNom: String = MessageObject.get("Nom").toString()
        val text1 = Nom + userNom
        nomPatientTextView.text = text1

        val Prenom: String = "PRENOM : "
        val userPrenom: String = MessageObject.get("Prenom").toString()
        val text2 = Prenom + userPrenom
        prenomTextView.text = text2

        val NumeroSecu: String = "N° : "
        val userNumeroSecu: String = MessageObject.get("NSecu").toString()
        val text3 = NumeroSecu + userNumeroSecu
        numeroSecuTtextView.text = text3

        val Email = "eMail : "
        val userEmail: String = MessageObject.get("Mail").toString()
        val text4 = Email + userEmail
        eMailTtextView.text = text4

        val Tel = "Tel : "
        val userTel: String = MessageObject.get("Tel").toString()
        val text5 = Tel + userTel
        telephoneTextView.text = text5

        val adress = "ADRESSE : "
        val userAdress: String = MessageObject.get("Adresse").toString()
        val text6 = adress + userAdress
        adresseTextView.text = text6

        val medecinT = "MEDECIN TRAITANT : "
        val userMedecin: String = MessageObject.get("Medecin").toString()
        val text7 = medecinT + userMedecin
        medecinTextView.text = text7

        modifierPatientButton.setOnClickListener {
            val intent = Intent(this, ModifierCountPatientActivity::class.java)

            intent.putExtra("USERNOM", userNom)
            intent.putExtra("USERPRENOM", userPrenom)
            intent.putExtra("USERNUMSECU", userNumeroSecu)
            intent.putExtra("USERMAIL", userEmail)
            intent.putExtra("USERTEL", userTel)
            intent.putExtra("USERADRESS", userAdress)
            intent.putExtra("USERMEDECIN", userMedecin)

            //A FAIRE : récupérer les infos de la carte en BLE

            startActivity(intent)
        }
        ButtonReturn.setOnClickListener {
            val intent = Intent(this, PatientEspaceActivity::class.java)
            startActivity(intent)
        }

    }

}
