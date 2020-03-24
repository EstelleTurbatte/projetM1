package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_consultation.*
import kotlinx.android.synthetic.main.activity_login_doctor.*

class ConsultationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation)

        ordonnaceButton.setOnClickListener {
            val intent = Intent(this, OrdonnanceActivity::class.java)
            startActivity(intent)
        }

        CRButton.setOnClickListener {
            val intent = Intent(this, CRActivity::class.java)
            startActivity(intent)
        }

        examenButton.setOnClickListener {
            val intent = Intent(this, ExamenActivity::class.java)

            startActivity(intent)
        }

        doctorCountButton.setOnClickListener {
            val intent = Intent(this, CountDoctorActivity::class.java)
            startActivity(intent)
        }
    }
}
