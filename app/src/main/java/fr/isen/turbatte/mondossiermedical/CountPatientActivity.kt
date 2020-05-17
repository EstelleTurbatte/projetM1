package fr.isen.turbatte.mondossiermedical

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_count_patient.*
import org.json.JSONObject


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

        messageRecu = intent.getStringExtra("MESSAGE_NORDIC")
        //Log.i("MESSAGE_RECU-BLE", messageRecu)
        val size:Int = messageRecu.length
        messageRecu = messageRecu.substring(12, size)
        Log.i("MESSAGE_RECU-BLE 1", messageRecu)
        val JSONObj = JSONObject()

       JSONObj.put("Commande", 6)

//test.get(KEY_LOGIN).toString()
        val gson = Gson()
        //var ordos:Ordonnances= gson.fromJson(listes_ordo, Ordonnances::class.java)

        //var MessageObject:Infopatient = gson.fromJson(messageRecu, Infopatient::class.java)
        val MessageObject1:JsonElement = gson.fromJson(messageRecu, JsonElement::class.java)
        val MessageObject2:JsonObject = gson.fromJson(MessageObject1, JsonObject::class.java)

        Log.i("MESSAGE_RECU-BLE JSON", "1 "+MessageObject1.toString())
        Log.i("MESSAGE_RECU-BLE JSON", "2  "+MessageObject2.toString())

        json = JSONObj.toString()

        val Nom: String = "NOM : "
        val userNom: String = MessageObject2.get("Nom").toString()
        val text1 = Nom + userNom
        nomPatientTextView.text = text1

        val Prenom: String = "PRENOM : "
        val userPrenom: String = MessageObject2.get("Prenom ").toString()
        val text2 = Prenom + userPrenom
        prenomTextView.text = text2

        val NumeroSecu: String = "N° : "
        val userNumeroSecu: String = MessageObject2.get("NSecu").toString()
        val text3 = NumeroSecu + userNumeroSecu
        numeroSecuTtextView.text = text3

        val Email = "eMail : "
        val userEmail: String = MessageObject2.get("Email").toString()
        val text4 = Email + userEmail
        eMailTtextView.text = text4

        val Tel = "Tel : "
        val userTel: String = MessageObject2.get("Tel").toString()
        val text5 = Tel + userTel
        telephoneTextView.text = text5

        val adress = "ADRESSE : "
        val userAdress: String = MessageObject2.get("Adresse").toString()
        val text6 = adress + userAdress
        adresseTextView.text = text6

        val medecinT = "MEDECIN TRAITANT : "
        val userMedecin: String = MessageObject2.get("Medecin").toString()
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


            startActivity(intent)
        }
        ButtonReturn.setOnClickListener {
            val intent = Intent(this, PatientEspaceActivity::class.java)
            startActivity(intent)
        }

    }

}
