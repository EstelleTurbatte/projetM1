package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login_patient.*

class LoginPatientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_patient)

        validerPatientButton.setOnClickListener {
            val intent = Intent(this, PatientEspaceActivity::class.java)
            startActivity(intent)
        }
    }
}
