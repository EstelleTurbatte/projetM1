package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        doctorButton.setOnClickListener{
            val intent = Intent(this, LoginDoctorActivity::class.java)
            startActivity(intent)
        }

        patientButton.setOnClickListener {
            val intent = Intent(this, LoginPatientActivity::class.java)
            startActivity(intent)
        }
    }

}
