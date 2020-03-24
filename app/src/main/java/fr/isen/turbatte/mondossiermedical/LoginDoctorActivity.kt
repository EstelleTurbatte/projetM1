package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login_doctor.*

class LoginDoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_doctor)

        val doctorLogin = loginDocteurInput.text.toString()
        val doctorPassword = passwordDoctorInput.text.toString()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("message")

        validerDoctorButton.setOnClickListener {
            val intent = Intent(this, ConsultationActivity::class.java)
            startActivity(intent)
        }

        newCountText.setOnClickListener {

        }
    }
}
