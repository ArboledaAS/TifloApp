package com.arboleda.tifloapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arboleda.tifloapp.databinding.ActivityAuthBinding
import com.arboleda.tifloapp.databinding.ActivityLoginBinding
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menus.MasterMenu
import com.arboleda.tifloapp.menus.SimpleMenuActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttomregresar.setOnClickListener {
            finish()
        }


        binding.accederButton.setOnClickListener {

            if (accederTextEmail.text!!.isNotEmpty() && accederTextPassword.text!!.isNotEmpty()){
                verificarLogin()
            }else{
                mostraralerta()
            }
        }

    }




    fun verificarLogin(){
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(accederTextEmail.text.toString().trim(),
                accederTextPassword.text.toString().trim()).addOnCompleteListener{
                var uid = it.result?.user?.uid.toString()
                val ref = FirebaseDatabase.getInstance().getReference("usuarios")
                ref.child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                var contenedor = snapshot.child("nivel").value

                                if (contenedor == "0"){
                                    startActivity(Intent(this@LoginActivity,MasterMenu::class.java))
                                    finish()
                                }
                                if (contenedor == "1"){
                                    startActivity(Intent(this@LoginActivity,SimpleMenuActivity::class.java))
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@LoginActivity, "No se pudo realizar la accion" +
                                    "debido a que: ${error.message}", Toast.LENGTH_LONG).show()
                        }

                    })

                /*
                if (it.isSuccessful){
                    db.collection("users").document(accederTextEmail.text.toString())
                        .get().addOnSuccessListener {documento ->
                            if (documento.exists()) {
                                var contenedor: String? = documento.getString("nivel")
                                if (contenedor == "0"){
                                    startActivity(Intent(this,MasterMenu::class.java))
                                    finish()
                                }
                                if (contenedor == "1"){
                                    startActivity(Intent(this,SimpleMenuActivity::class.java))
                                    finish()

                                }
                            }

                        }


                } else{
                    Toast.makeText(this,"Error en la autenticacion", Toast.LENGTH_SHORT).show()
                }*/

            }.addOnFailureListener {
                Toast.makeText(this, "No se pudo haceder debido a que: ${it.message} ", Toast.LENGTH_LONG).show()
            }
    }



    fun forgotPassword(view: View){
        startActivity(Intent(this,ForgotPassActivity::class.java))
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