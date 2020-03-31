package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_patient_espace.*

class PatientEspaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_espace)

        ordonnaceButton2.setOnClickListener {
            val intent = Intent(this, MesOrdonnancesActivity::class.java)
            startActivity(intent)
        }

        CRButton2.setOnClickListener {
            val intent = Intent(this, MesCRActivity::class.java)
            startActivity(intent)
        }

        examenButton2.setOnClickListener {
            val intent = Intent(this, MesExamsActivity::class.java)
            startActivity(intent)
        }

        patientCountButton.setOnClickListener {
            val intent = Intent(this, CountPatientActivity::class.java)
            startActivity(intent)
        }

        BluetoothButton.setOnClickListener {
            val intent = Intent(this, BLEActivity::class.java)
            startActivity(intent)

        }
    }
}
