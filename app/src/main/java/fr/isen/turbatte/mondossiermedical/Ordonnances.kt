package fr.isen.turbatte.mondossiermedical
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Ordonnances : Parcelable {
    val results: ArrayList<Results> = ArrayList()
}