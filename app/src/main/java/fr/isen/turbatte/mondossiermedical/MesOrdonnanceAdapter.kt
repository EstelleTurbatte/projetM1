package fr.isen.turbatte.mondossiermedical

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ordo_cell_ble.view.*

class MesOrdonnanceAdapter(
    private val ordonnance: Ordonnances,
    val context: Context,
    private val ordonnanceClickListener: (Results) -> Unit
) :
    RecyclerView.Adapter<MesOrdonnanceAdapter.MesOrdonnanceHolder>() {

    class MesOrdonnanceHolder(
        ordonnanceView: View,
        private val ordonnance: Ordonnances,
        private val ordonnanceClickListener: (Results) -> Unit
    ):
        RecyclerView.ViewHolder(ordonnanceView) {

        private val medecin: TextView = ordonnanceView.medecinTextviewble
        private val date: TextView = ordonnanceView.DateTextView
        private val id: TextView = ordonnanceView.IDtextView
        private val layout = ordonnanceView.ordonnanceLayout

        fun pushInfo(position: Int) {
            medecin.text = ordonnance.results[position].Medecin
            date.text = ordonnance.results[position].Date
            id.text = ordonnance.results[position].Id.toString()

            layout.setOnClickListener{
                ordonnanceClickListener.invoke(ordonnance.results[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesOrdonnanceHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.ordo_cell_ble, parent, false)
        return MesOrdonnanceHolder(view, ordonnance, ordonnanceClickListener)
    }

    override fun getItemCount(): Int = ordonnance.results.size

    override fun onBindViewHolder(holder: MesOrdonnanceHolder, position: Int) {
        holder.pushInfo(position)
    }

}
