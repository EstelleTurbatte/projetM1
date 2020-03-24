package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ordonnance.*

class OrdonnanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordonnance)

        newOrdonnanceButton.setOnClickListener {
            val intent = Intent(this, NewOrdonnanceActivity::class.java)
            startActivity(intent)
        }
    }
}
