package fr.isen.turbatte.mondossiermedical

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_patient.*

class LoginPatientActivity : AppCompatActivity() {

    val loginClair = "admin"
    val motPasseClair = "123"
    val KEY_LOGIN = "NOM"
    val KEY_PASSWORD = "pass"
    lateinit var sharedPreferences: SharedPreferences
    private val USER_PREFS = "user_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_patient)

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val savedIdentifiant: String? = sharedPreferences.getString(KEY_LOGIN, "")
        val savedPassword: String? = sharedPreferences.getString(KEY_PASSWORD, "")

        if (savedIdentifiant == loginClair && savedPassword == motPasseClair) {
            goToHome()
        }

        validerPatientButton.setOnClickListener {
            val intent = Intent(this, PatientEspaceActivity::class.java)
            startActivity(intent)

            val userlogin = loginPatient.text.toString()
            val userpassword = passwordPatient.text.toString()

            if (userlogin == loginClair && userpassword == motPasseClair) {
                //Toast.makeText(this, "Identification réussie", Toast.LENGTH_LONG).show()
                saveCredential(userlogin, userpassword)
                goToHome()
            } else {
                Toast.makeText(this, "Accès refusé", Toast.LENGTH_LONG).show()
            }
        }
        logOut()
    }

    private fun saveCredential(id: String, pass: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LOGIN, id)
        editor.putString(KEY_PASSWORD, pass)

        editor.apply()
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("login", loginClair)
        startActivity(intent)
        finish()
    }

    private fun logOut() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_LOGIN)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }
}
