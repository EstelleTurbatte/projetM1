package fr.isen.turbatte.mondossiermedical

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_count_patient.*

class CountPatientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_patient)

        val Nom: String = "NOM : "
        val userNom: String = "eh"
        val text1 = Nom + userNom
        nomPatientTextView.text = text1

        val Prenom: String = "PRENOM : "
        val userPrenom: String = "eh"
        val text2 = Prenom + userPrenom
        prenomTextView.text = text2

        val NumeroSecu: String = "N° : "
        val userNumeroSecu: String = "eh"
        val text3 = NumeroSecu + userNumeroSecu
        numeroSecuTtextView.text = text3

        val Email = "eMail : "
        val userEmail: String = "eh"
        val text4 = Email + userEmail
        eMailTtextView.text = text4

        val Tel = "Tel : "
        val userTel: String = "eh"
        val text5 = Tel + userTel
        telephoneTextView.text = text5

        val adress = "ADRESSE : "
        val userAdress: String = "eh"
        val text6 = adress + userAdress
        adresseTextView.text = text6

        val medecinT = "MEDECIN TRAITANT : "
        val userMedecin: String = "eh"
        val text7 = medecinT + userMedecin
        medecinTextView.text = text7

        modifierPatientButton.setOnClickListener {
            val intent = Intent(this, ModifierCountPatientActivity::class.java)

            intent.putExtra("USERNOM", userNom)
            intent.putExtra("USERPRENOM", userPrenom)
            intent.putExtra("USERNUMSECU", userNumeroSecu)
            intent.putExtra("USERMAIL", userEmail)
            intent.putExtra("USERTEL", userTel)
            intent.putExtra("USERADRESS", userAdress)
            intent.putExtra("USERMEDECIN", userMedecin)

            //A FAIRE : récupérer les infos de la carte en BLE

            startActivity(intent)
        }


    }
}
