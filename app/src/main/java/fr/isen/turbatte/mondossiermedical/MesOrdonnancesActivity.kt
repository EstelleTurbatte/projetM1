package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_mes_ordonnances.*
import kotlinx.android.synthetic.main.activity_ordonnance.*

class MesOrdonnancesActivity : AppCompatActivity() {

    private var listeOrdo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_ordonnances)
        val ordo = Ordonnances()

        val gson = Gson()

        listeOrdo = intent.getStringExtra("MESSAGE_BLE")
        val size = listeOrdo.length
        Log.i("BLE_ORDO-1", listeOrdo)
        listeOrdo = listeOrdo.substring(12, size-2)

        Log.i("BLE_ORDO-2", listeOrdo)

        val ordos:Ordonnances= gson.fromJson(listeOrdo, Ordonnances::class.java)
        ordos.results.forEachIndexed { idx, tut -> Log.i("NDEF_MESSAGE", "ITEM : $idx :\n $tut")
            mesOrdonnancesRecycler.adapter = MesOrdonnanceAdapter(ordos,this, ::onOrdonnanceClicked)
            mesOrdonnancesRecycler.layoutManager = LinearLayoutManager(this)
            mesOrdonnancesRecycler.visibility = View.VISIBLE
        }

    }
    private fun onOrdonnanceClicked(Ordonnance: Results) {
        val intent = Intent(this, MesOrdonnancesVisibilityActivity::class.java)
        intent.putExtra("Ordo", Ordonnance)
        startActivity(intent)
    }
}
