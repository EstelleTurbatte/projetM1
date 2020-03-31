package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mes_c_r.*

class MesCRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_c_r)

        val cr = CompteRendus()

        mesCRRecycler.adapter = MesCRAdapter(cr, ::onCRClicked)
        mesCRRecycler.layoutManager = LinearLayoutManager(this)
    }
    private fun onCRClicked(device: CompteRendus) {
        val intent = Intent(this, MesCrVisibilityActivity::class.java)
        startActivity(intent)
    }

}
