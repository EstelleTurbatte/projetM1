package fr.isen.turbatte.mondossiermedical

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ResultsCR (
    val Date: String,
    val MedecinPrescripteur: String,
    val id: Int
) : Parcelable
