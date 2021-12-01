package com.arboleda.tifloapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menus.MasterMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        accederButton.setOnClickListener {

            if (accederTextEmail.text.isNotEmpty() && accederTextPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(accederTextEmail.text.toString(),
                        accederTextPassword.text.toString()).addOnCompleteListener{
                            if (it.isSuccessful){
                                //startActivity(Intent(this,CreateBook::class.java))

                                db.collection("users").document(accederTextEmail.text.toString())
                                    .get().addOnSuccessListener {documento ->
                                        if (documento.exists()) {
                                            var contenedor: String? = documento.getString("nivel")
                                            if (contenedor == "0"){
                                                startActivity(Intent(this,MasterMenu::class.java))
                                            }
                                            if (contenedor == "1"){
                                                setContentView(R.layout.simple_menu)
                                            }
                                        }

                                    }


                                    } else{
                                Toast.makeText(this,"Error en la autenticacion", Toast.LENGTH_SHORT).show()}

                    }
            }else{
                mostraralerta()
            }
        }

    }
    private fun mostraralerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}