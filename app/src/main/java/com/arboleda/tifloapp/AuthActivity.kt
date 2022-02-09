package com.arboleda.tifloapp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.arboleda.tifloapp.databinding.ActivityAuthBinding
import com.arboleda.tifloapp.databinding.ActivityPoesiaAddBinding
import com.arboleda.tifloapp.menus.MasterMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_login.*
import java.net.PasswordAuthentication

class AuthActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAuthBinding

    private val db = FirebaseFirestore.getInstance()
    private var prueba:Int? = null

    private lateinit var  firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_auth)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)




        /////////// Seleccion de administrador de spinner
        val spinner = findViewById<Spinner>(R.id.opcionesSpinner)
        val lista = listOf("Administrador", "Creador de contenido")
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)

        spinner.adapter = adaptador

        spinner.onItemSelectedListener = object:
        AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO("Not yet implemented")
                prueba = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        /////////// Seleccion de administrador de spinner***********

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere Porfavor")
        progressDialog.setCanceledOnTouchOutside(false)



        ////////Botton de registrar
        binding.registrarButton.setOnClickListener {
            createUser()
        }
        ////////Botton de registrar*******

        binding.buttomregresar.setOnClickListener {
            finish()
        }


    }//clase




    private fun createUser(){
        val email = emailEditText.text.toString().trim()
        val contrasena = passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && contrasena.isNotEmpty()){
            progressDialog.setMessage("Creando cuenta")
            progressDialog.show()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,contrasena)
                .addOnSuccessListener {
                    var uid = it.user?.uid.toString()
                    authuserfirebasedb(uid)
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "No se pudo crear debido a: ${it.message}", Toast.LENGTH_LONG).show()
                }

        }else{
            //mostraralerta()
            Toast.makeText(this, "Porfavor llene los campos de email y contraseña", Toast.LENGTH_LONG).show()
        }


        ///////////
    }


    private fun authuserfirebasedb(uid:String) {
        val email = emailEditText.text.toString().trim()


        progressDialog.setMessage("Guardando informacion del usuario")

        Toast.makeText(this,"Se creo usuario",Toast.LENGTH_SHORT).show()

        val dbReference = FirebaseDatabase.getInstance().getReference("usuarios")
                dbReference.child(uid).setValue(hashMapOf(
                "id" to uid,
                "email" to email,
                "nivel" to "$prueba"))
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Se creo la cuenta exitosamente....", Toast.LENGTH_LONG).show()
                            emailEditText.setText(null)
                            passwordEditText.setText(null)
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "No se pudo realizar debido a ${it.message}", Toast.LENGTH_LONG).show()

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




    ////////////////inicializa el menu escritor y lector
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //R.id.menu_escritor -> startActivity(Intent(this,CreateBook::class.java))
            R.id.menu_escritor -> startActivity(Intent(this,LoginActivity::class.java))
            R.id.menu_lector -> startActivity(Intent(this,AuthActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    ////////////////inicializa el menu escritor y lector*******





}