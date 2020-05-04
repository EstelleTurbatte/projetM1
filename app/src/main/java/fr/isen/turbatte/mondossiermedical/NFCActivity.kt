package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import android.nfc.*
import android.nfc.NdefRecord.createMime
import android.nfc.tech.IsoDep
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_n_f_c.*
import java.nio.charset.Charset

class NFCActivity : AppCompatActivity(), NfcAdapter.ReaderCallback, NfcAdapter.CreateNdefMessageCallback {

    private var nfcAdapter: NfcAdapter? = null
    var messageAEnvoyer= ""
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_n_f_c)
        messageAEnvoyer = intent.getStringExtra("MESSAGE")
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        textView = textViewNFC

        if (!nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                this,
                "NFC disabled on this device. Turn on to proceed",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (nfcAdapter?.isEnabled!!) {
            Toast.makeText(
                this,
                "NFC OK",
                Toast.LENGTH_SHORT
            ).show()
        }
        nfcAdapter?.setNdefPushMessageCallback(this, this)
        val mimeRecord = NdefRecord.createMime(
            "application/vnd.com.example.android.beam",
            messageAEnvoyer.toByteArray(Charset.forName("US-ASCII"))
        )
    }
//recevoir un message NDEF
    /*override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // Process the messages array.
                val taille = messages.size
                for (i in messages) {
                    Log.i("message", i)
                }
            }
        }
    }*/

    override fun onTagDiscovered(tag: Tag?) {
        val isoDep = IsoDep.get(tag)
        isoDep.connect()
        val response = isoDep.transceive(Utils.hexStringToByteArray(
            "00A4040007A0000002471001"))
        runOnUiThread { textView.append("\nCard Response: "
                + Utils.toHex(response)) }
        isoDep.close()
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(this, this,
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                        NfcAdapter.FLAG_READER_NFC_A,
                        null)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            processIntent(intent)
        }
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private fun processIntent(intent: Intent) {
        textView = textViewNFC
        // only one message sent during the beam
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
            (rawMsgs[0] as NdefMessage).apply {
                // record 0 contains the MIME type, record 1 is the AAR, if present
                textView.text = String(records[0].payload)
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // onResume gets called after this to handle the intent
        setIntent(intent)
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    override fun createNdefMessage(event: NfcEvent?): NdefMessage {
        val text = messageAEnvoyer +
                "Beam Time: " + System.currentTimeMillis()
        return NdefMessage(
            arrayOf(
                createMime("application/vnd.com.example.android.beam", text.toByteArray())
            )
            /**
             * The Android Application Record (AAR) is commented out. When a device
             * receives a push with an AAR in it, the application specified in the AAR
             * is guaranteed to run. The AAR overrides the tag dispatch system.
             * You can add it back in to guarantee that this
             * activity starts when receiving a beamed message. For now, this code
             * uses the tag dispatch system.
             *///,NdefRecord.createApplicationRecord("com.example.android.beam")
        )
    }
}


