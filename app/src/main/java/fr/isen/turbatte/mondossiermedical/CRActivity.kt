package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
        val cr = CompteRendus()

        crRecyclerView.adapter = CRAdapter(cr, ::onCRClicked)
        crRecyclerView.layoutManager = LinearLayoutManager(this)

    }
    private fun onCRClicked(device: CompteRendus) {
        val intent = Intent(this, CRVisibilityActivity::class.java)
        startActivity(intent)
    }
}

