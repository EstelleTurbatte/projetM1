package fr.isen.turbatte.mondossiermedical

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ExamenActivity : AppCompatActivity() {

    private lateinit var adapter: ExamenAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_examen)
    }
}
