package fr.isen.turbatte.mondossiermedical

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_ordonnance.*
import org.json.JSONObject
import java.io.IOException

class OrdonnanceActivity : AppCompatActivity() {

    private lateinit var adapter: OrdonnanceAdapter
    private var id:Int = 4
    private var commande:Int = 0
    private var tagTrouve:Boolean = false
    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null
    private val KEY_LOG_TEXT = "logText"
    private var ndef: Ndef? = null
    private var listes_ordo = ""
    private var json = ""
    private var autorisation:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordonnance)

        newOrdonnanceButton.setOnClickListener {
            val intent = Intent(this, NewOrdonnanceActivity::class.java)
            id ++
            Log.i("id", id.toString())
            intent.putExtra("id", id)
            startActivity(intent)
        }
        val ordo = Ordonnances()

        //ordonanceRecycler.adapter = OrdonnanceAdapter(ordo, ::onOrdonnanceClicked)
        //ordonanceRecycler.layoutManager = LinearLayoutManager(this)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        val JSONObj = JSONObject()

        JSONObj.put("Commande", commande)

        if (intent != null) {
            processIntent(intent)
        }

        json = JSONObj.toString()

        //val cr = CompteRendus()

        val builder = AlertDialog.Builder(this@OrdonnanceActivity)
        val editView = View.inflate(this@OrdonnanceActivity, R.layout.alert_dialog_builder, null)

        builder.setView(editView)
        if (tagTrouve==false){
            builder.setIcon(R.drawable.no_nfc)
            builder.setTitle("VEUILLEZ TROUVER UN TAG NFC")
        }else{
            builder.setIcon(R.drawable.nfc_ok)
            builder.setTitle("TAG NFC TROUVE")
        }
        builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { _, _ -> })
        builder.setPositiveButton("Valider", DialogInterface.OnClickListener { _, _ ->})
        builder.show()


        /*val gson = GsonBuilder().create()
        //listes_ordo = "[{\"Date\":  \"25/03/2019\",\"MedecinPrescripteur\":  \"Toto\",\"id\":  0}, {\"Date\":  \"25/03/2020\",\"MedecinPrescripteur\":  \"Toto\",\"id\":  0}]"
        var homedateList = CompteRendus()
        homedateList = gson.fromJson(listes_ordo, CompteRendus::class.java)
        crRecyclerView.adapter = CRAdapter(homedateList,this, ::onCRClicked)
        crRecyclerView.layoutManager = LinearLayoutManager(this)
        crRecyclerView.visibility = View.VISIBLE*/


    }
    private fun onOrdonnanceClicked(Ordonnance: Results) {
        val intent = Intent(this, OrdonnanceVisibilityActivity::class.java)
        intent.putExtra("Ordo", Ordonnance)
        startActivity(intent)
    }

    fun createNFCMessage(payload: String, intent: Intent?) : Boolean {
        tagTrouve = true
        val msg = payload

        val pathPrefix = "estelleturbatte.com:mondossiermedical"
        val nfcRecord = NdefRecord.createTextRecord("fr", msg)
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return  writeMessageToTag(nfcMessage, tag)
        }
        return false
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    //Message to large to write to NFC tag
                    return false
                }
                if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    //Message is written to tag
                    return true
                } else {
                    //NFC tag is read-only
                    return false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    //The data is written to the tag
                    return true
                } catch (e: IOException) {
                    //Failed to format tag
                    return false
                }
            }
            //NDEF is not supported
            return false

        } catch (e: Exception) {
            //Write operation has failed
        }
        return false
    }
    override fun onResume() {
        super.onResume()
        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null);
        // Alternative: only get specific HTTP NDEF intent
        //nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilters, null);
    }

    override fun onPause() {
        super.onPause()
        // Disable foreground dispatch, as this activity is no longer in the foreground
        nfcAdapter?.disableForegroundDispatch(this);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        //logMessage("Found intent in onNewIntent", intent?.action.toString())
        // If we got an intent while the app is running, also check if it's a new NDEF message
        // that was discovered
        //if (intent != null) processIntent(intent)
        //val messageWrittenSuccessfully = createNFCMessage(json, intent)
        //Log.i("JSON : ", messageFinal)
        //textViewResult.text = ifElse(messageWrittenSuccessfully,"Successful Written to Tag","Something went wrong Try Again")

        var cpt = 0;
        val tagFromIntent = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        ndef = Ndef.get(tagFromIntent)
        // Log.i("NDEF_MESSAGE", ndef.toString())
        if (autorisation){
            Log.i("NDEF_MESSAGE", "auto 1 : $autorisation")
            val messageWrittenSuccessfully = createNFCMessage(json, intent)
            textView4.text = ifElse(messageWrittenSuccessfully,"Successful Written to Tag","Something went wrong Try Again")
            autorisation = false
        }else{
            Log.i("NDEF_MESSAGE", "auto 2 : $autorisation")
            cpt++
            intent?.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
                (rawMsgs[0] as NdefMessage).apply {
                    listes_ordo = String(records[0].payload)
                }
            }
        }
        if (cpt>0){
            Log.i("NDEF_MESSAGE", "auto 3 : $autorisation")
            val size_lsite = listes_ordo.length
            listes_ordo = listes_ordo.substring(3,size_lsite-2)
            Log.i("NDEF_MESSAGE 3", listes_ordo)

            val gson = Gson()

            val toto:String = "{[{\"Date\":	\"2020-05-14T16:29:13.277\",\"Medecin\":\"Docteur Magali Milanini\",\"Id\":	0},{\"Date\":\"2020-05-14T17:13:11.872\",\"Medecin\":\"Docteur Magali Milanini\",\"Id\":3 },{\"Date\":\"2020-05-14T16:59:21.492\",\"Medecin\":	\"Docteur Magali Milanini\",\"Id\":	0 }]}"
            val tableau = object : TypeToken<Array<Results>>() {}.type

            val ordos:Ordonnances= gson.fromJson(listes_ordo, Ordonnances::class.java)
            ordos.results.forEachIndexed { idx, tut -> Log.i("NDEF_MESSAGE", "ITEM : $idx :\n $tut")
                ordonanceRecycler.adapter = OrdonnanceAdapter(ordos,this, ::onOrdonnanceClicked)
                ordonanceRecycler.layoutManager = LinearLayoutManager(this)
                ordonanceRecycler.visibility = View.VISIBLE
            }


            /*var ordos:Array<Results>= gson.fromJson(listes_ordo, Array<Results>::class.java)

            ordos.results.forEachIndexed { idx, tut -> Log.i("NDEF_MESSAGE", "ITEM : $idx :\n $tut")
                ordonanceRecycler.adapter = OrdonnanceAdapter(ordos,this, ::onOrdonnanceClicked)
                ordonanceRecycler.layoutManager = LinearLayoutManager(this)
                ordonanceRecycler.visibility = View.VISIBLE
            }*/


        }


        //Gson().fromJson(jsonString, Array<Video>::class.java)

//        val homedateList: List<CompteRendus> = gson.fromJson(listes_ordo, Array<CompteRendus>::class.java).toList()

        //val arrayTutorialType = object : TypeToken<CompteRendus>() {}.type

        //val jsonString: CompteRendus = gson.fromJson(listes_ordo, arrayTutorialType)

        //crRecyclerView.adapter = CRAdapter(homedateList,this, ::onCRClicked)
        // crRecyclerView.layoutManager = LinearLayoutManager(this)

        /* var i = 0
         for (compteR in homedateList){
             Log.i("CR_adapter", i.toString())
             Log.i("CR_adapter", i.toString())
            ordonanceRecycler.adapter = CRAdapter(compteR,this, ::onCRClicked)
            ordonanceRecycler.layoutManager = LinearLayoutManager(this)
            ordonanceRecycler.visibility = View.VISIBLE
             i++
         }*/

        //getUserFromApi(listes_ordo)

    }

    fun<T> ifElse(condition: Boolean, primaryResult: T, secondaryResult: T) = if (condition) primaryResult else secondaryResult

    private fun processIntent(checkIntent: Intent) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            //logMessage("New NDEF intent", checkIntent.toString())

            // Retrieve the raw NDEF message from the tag
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            //logMessage("Raw messages", rawMessages.size.toString())

            // Complete variant: parse NDEF messages
            if (rawMessages != null) {
                val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)// Array<NdefMessage>(rawMessages.size, {})
                for (i in rawMessages.indices) {
                    messages[i] = rawMessages[i] as NdefMessage;
                }
                // Process the messages array.
                processNdefMessages(messages)
            }

            // only one message sent during the beam
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
                (rawMsgs[0] as NdefMessage).apply {
                    // record 0 contains the MIME type, record 1 is the AAR, if pre
                }
            }
        }
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        // Go through all NDEF messages found on the NFC tag
        for (curMsg in ndefMessages) {
            if (curMsg != null) {

                // Loop through all the records contained in the message
                for (curRecord in curMsg.records) {
                    if (curRecord.toUri() != null) {
                        // URI NDEF Tag
                    } else {
                        // Other NDEF Tags - simply print the payload
                    }
                }
            }
        }
    }

    private fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }

}