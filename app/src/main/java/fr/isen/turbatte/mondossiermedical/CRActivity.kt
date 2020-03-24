package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cr.*

class
CRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cr)

        newCRButton.setOnClickListener {
            val intent = Intent(this, NewCRActivity::class.java)
            startActivity(intent)
        }
    }
}
