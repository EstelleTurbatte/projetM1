package fr.isen.turbatte.mondossiermedical

import android.app.AlertDialog
import android.content.Context
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
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cr.*
import org.json.JSONObject
import java.io.IOException

class
CRActivity : AppCompatActivity() {

    private var id:Int = 4
    private var commande:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cr)



        val JSONObj = JSONObject()

        JSONObj.put("Commande", commande)

        val json = JSONObj.toString()

        newCRButton.setOnClickListener {
            val intent = Intent(this, NewCRActivity::class.java)
            id ++
            intent.putExtra("ID_CR",id)
            startActivity(intent)
        }
        val cr = CompteRendus()

        crRecyclerView.adapter = CRAdapter(cr, ::onCRClicked)
        crRecyclerView.layoutManager = LinearLayoutManager(this)

        val messageWrittenSuccessfully = createNFCMessage(json, intent)

        val builder = AlertDialog.Builder(this@CRActivity)
        val editView = View.inflate(this@CRActivity, R.layout.alert_dialog_builder, null)
        builder.setView(editView)
        builder.setTitle("VEUILLEZ TROUVER UN TAG NFC")
        builder.setNegativeButton("Annuler", DialogInterface.OnClickListener { _, _ -> })
        builder.setPositiveButton("Valider", DialogInterface.OnClickListener { _, _ ->})
        builder.show()

    }
    private fun onCRClicked(device: CompteRendus) {
        val intent = Intent(this, CRVisibilityActivity::class.java)
        startActivity(intent)
    }

    fun createNFCMessage(payload: String, intent: Intent?) : Boolean {

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
}

