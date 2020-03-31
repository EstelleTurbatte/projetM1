package fr.isen.turbatte.mondossiermedical

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.exam_cell.view.*
import kotlinx.android.synthetic.main.ordonnance_cell.view.*

class ExamenAdapter(
    private val examen: Examens,
    private val examenClickListener: (Examens) -> Unit
) :
    RecyclerView.Adapter<ExamenAdapter.ExamenHolder>() {

    class ExamenHolder(
        examenView: View,
        private val examen: Examens,
        private val examenClickListener: (Examens) -> Unit
    ) :
        RecyclerView.ViewHolder(examenView) {

        private val motif: TextView = examenView.motifTextView
        private val date: TextView = examenView.dateView
        private val type: TextView = examenView.typeTextView
        private val layout = examenView.ordonnanceLayout

        fun pushInfo(position: Int) {
            motif.text = examen.resultExam[position].motif
            date.text = examen.resultExam[position].date
            type.text = examen.resultExam[position].type

            layout.setOnClickListener {
                examenClickListener.invoke(examen)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamenHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.exam_cell, parent, false)
        return ExamenHolder(view, examen, examenClickListener)
    }

    override fun getItemCount(): Int = examen.resultExam.size

    override fun onBindViewHolder(holder: ExamenHolder, position: Int) {
        holder.pushInfo(position)
    }
}

