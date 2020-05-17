package fr.isen.turbatte.mondossiermedical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_mes_ordonnances_visibility.*

class MesOrdonnancesVisibilityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_ordonnances_visibility)

        val ordo: Results = intent.getParcelableExtra("Ordo")

        dateBle.text = ordo.Date
        infoDocBle.text = ordo.Medecin
    }
}
