package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_new_cr.*
import org.json.JSONObject
import java.time.LocalDateTime

class NewCRActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_cr)

        val id = intent.getStringExtra("ID_CR")

        val date = LocalDateTime.now()
        val medecin = infoMedecin2.text.toString()
        val motif = motifTextView3.text.toString()
        val prescription = compteRenduTexView.text.toString()

        dateTextView2.text = date.toString()

        val JSONObj = JSONObject()

        JSONObj.put("Commande", 8)
        JSONObj.put("Date", date)
        JSONObj.put("Motif", motif)
        JSONObj.put("MedecinPrescripteur", medecin)
        JSONObj.put("CompteRendu", prescription)
        JSONObj.put("Id", id)

        val json = JSONObj.toString()

        envoyerButton2.setOnClickListener {
            val intent = Intent(this, NFC_CR_Activity::class.java)
            intent.putExtra("JSON_CR",json)
            startActivity(intent)
        }
    }
}
