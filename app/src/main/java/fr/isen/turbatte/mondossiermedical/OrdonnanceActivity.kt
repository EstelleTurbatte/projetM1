package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ordonnance.*

class OrdonnanceActivity : AppCompatActivity() {

    private lateinit var adapter: OrdonnanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordonnance)

        newOrdonnanceButton.setOnClickListener {
            val intent = Intent(this, NewOrdonnanceActivity::class.java)
            startActivity(intent)
        }
        val ordo = Ordonnances()

        ordonanceRecycler.adapter = OrdonnanceAdapter(ordo, ::onOrdonnanceClicked)
        ordonanceRecycler.layoutManager = LinearLayoutManager(this)

    }
    private fun onOrdonnanceClicked(device: Ordonnances) {
        val intent = Intent(this, OrdonnanceVisibilityActivity::class.java)
        startActivity(intent)
    }
}
