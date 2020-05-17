package fr.isen.turbatte.mondossiermedical

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class Results (
    //val Motif: String,
    val Date: String,
    val Id: Int,
    //val Medicament: String,
    val Medecin: String
): Parcelable


