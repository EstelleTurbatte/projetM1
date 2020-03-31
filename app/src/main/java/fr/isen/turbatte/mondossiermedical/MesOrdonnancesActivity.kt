package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mes_ordonnances.*
import kotlinx.android.synthetic.main.activity_ordonnance.*

class MesOrdonnancesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_ordonnances)
        val ordo = Ordonnances()

        mesOrdonnancesRecycler.adapter = MesOrdonnanceAdapter(ordo, ::onOrdonnanceClicked)
        mesOrdonnancesRecycler.layoutManager = LinearLayoutManager(this)

    }
    private fun onOrdonnanceClicked(device: Ordonnances) {
        val intent = Intent(this, OrdonnanceVisibilityActivity::class.java)
        startActivity(intent)
    }
}
