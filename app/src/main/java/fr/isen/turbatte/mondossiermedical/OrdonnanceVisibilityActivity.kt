package fr.isen.turbatte.mondossiermedical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ordonnance_visibility.*

class OrdonnanceVisibilityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordonnance_visibility)

        val ordo: Ordonnances = intent.getParcelableExtra("Ordos")


    }
}
