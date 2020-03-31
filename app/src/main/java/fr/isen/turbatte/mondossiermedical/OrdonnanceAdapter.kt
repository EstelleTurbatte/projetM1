package fr.isen.turbatte.mondossiermedical

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ordonnance_cell.view.*

class OrdonnanceAdapter(
    private val ordonnance: Ordonnances,
    private val ordonnanceClickListener: (Ordonnances) -> Unit
) :
    RecyclerView.Adapter<OrdonnanceAdapter.OrdonnanceHolder>() {

    class OrdonnanceHolder(
        ordonnanceView: View,
        private val ordonnance: Ordonnances,
        private val ordonnanceClickListener: (Ordonnances) -> Unit
    ):
        RecyclerView.ViewHolder(ordonnanceView) {

        private val motif: TextView = ordonnanceView.motifTextView
        private val date: TextView = ordonnanceView.dateView
        private val layout = ordonnanceView.ordonnanceLayout

        fun pushInfo(position: Int) {
            motif.text = ordonnance.results[position].motif
            date.text = ordonnance.results[position].date

            layout.setOnClickListener{
                ordonnanceClickListener.invoke(ordonnance)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdonnanceHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.ordonnance_cell, parent, false)
        return OrdonnanceHolder(view, ordonnance, ordonnanceClickListener)
    }

    override fun getItemCount(): Int = ordonnance.results.size

    override fun onBindViewHolder(holder: OrdonnanceHolder, position: Int) {
        holder.pushInfo(position)
    }

}
