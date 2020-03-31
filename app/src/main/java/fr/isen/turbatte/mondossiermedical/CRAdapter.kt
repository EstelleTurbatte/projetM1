package fr.isen.turbatte.mondossiermedical

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cr_cell.view.*

class CRAdapter(
    private val compteRendus: CompteRendus,
    private val compteRendusClickListener: (CompteRendus) -> Unit
) :
    RecyclerView.Adapter<CRAdapter.CompteRendusHolder>() {

    class CompteRendusHolder(
        crView: View,
        private val compteRendus: CompteRendus,
        private val compteRendusClickListener: (CompteRendus) -> Unit
    ):
        RecyclerView.ViewHolder(crView) {
        private val motif: TextView = crView.motifTextView2
        private val date: TextView = crView.dateView3
        private val layout = crView.crLayout

        fun pushInfo(position: Int) {
            motif.text = compteRendus.results[position].motif
            date.text = compteRendus.results[position].date

            layout.setOnClickListener {
                compteRendusClickListener.invoke(compteRendus)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompteRendusHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.ordonnance_cell, parent, false)
        return CompteRendusHolder(view, compteRendus, compteRendusClickListener)
    }

    override fun getItemCount(): Int = compteRendus.results.size

    override fun onBindViewHolder(holder: CompteRendusHolder, position: Int) {
        holder.pushInfo(position)
    }

}