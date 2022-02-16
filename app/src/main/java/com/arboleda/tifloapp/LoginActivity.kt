package com.arboleda.tifloapp

import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.arboleda.tifloapp.view.FirstUserListActivity
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

    private lateinit var progressDialog: ProgressDialog


    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere Porfavor")
        progressDialog.setCanceledOnTouchOutside(false)


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
        progressDialog.setMessage("Iniciando sesión")
        progressDialog.show()
        //Acceder al servicio de firebase auth la cual espera un email y una contraseña
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(binding.accederTextEmail.text.toString().trim(),
                binding.accederTextPassword.text.toString().trim()).addOnCompleteListener{
                    //Si se completa el registro del usuario ejecutara lo que esta en el IF
                    if (it.isSuccessful){
                        //obtiene el uid del usuario registrado
                        var uid = it.result?.user?.uid.toString()
                        //busca en la base de datos el usuario con el uid obtenido
                        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
                        ref.child(uid)
                                .addListenerForSingleValueEvent(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        //si el usuario existe ejecutara el interior del IF
                                        if (snapshot.exists()){
                                            //Obtiene de la base de datos el valor del apartado nivel
                                            var contenedor = snapshot.child("nivel").value

                                            if (contenedor == "0"){
                                                progressDialog.dismiss()
                                                startActivity(Intent(this@LoginActivity,MasterMenu::class.java))
                                                finish()
                                            }
                                            else if (contenedor == "1"){
                                                progressDialog.dismiss()
                                                startActivity(Intent(this@LoginActivity,SimpleMenuActivity::class.java))
                                                finish()
                                            }else{
                                                progressDialog.dismiss()
                                                Toast.makeText(this@LoginActivity, "No se encontro en la base de datos", Toast.LENGTH_LONG).show()

                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@LoginActivity, "No se pudo realizar la accion" +
                                                "debido a que: ${error.message}", Toast.LENGTH_LONG).show()
                                    }

                                })



                        /////////
                    }

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
                    progressDialog.dismiss()
                Toast.makeText(this, "No se pudo acceder debido a que: ${it.message} ", Toast.LENGTH_LONG).show()
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