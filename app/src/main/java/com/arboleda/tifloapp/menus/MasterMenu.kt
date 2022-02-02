package com.arboleda.tifloapp.menus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.arboleda.tifloapp.AuthActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.menulibros.ContentAddActivity
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menulibros.DeleteBook
import com.arboleda.tifloapp.menulibros.FilesAddActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_master_menu.*

class MasterMenu : AppCompatActivity() {
    var PosicionSpinner: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_menu)

        /*
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion exitosa con la base de datos")
        analytics.logEvent("InitScreen", bundle)*/

        /*
        button1.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        button2.setOnClickListener {
            startActivity(Intent(this, CreateBook::class.java))
        }
        button3.setOnClickListener {

        }

         */

        ///////SPINNER SELECCION
        val spinner = findViewById<Spinner>(R.id.spnElementos)

        val lista = listOf("Agregar Usuario","Crear libro","Agregar contenido al libro",
                "Agregar archivos a la poesia","Eliminar Libro" )

        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter = adaptador
        ///////SPINNER SELECCION
        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                PosicionSpinner = position

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                actualizartextview(PosicionSpinner)
                handler.postDelayed(this, 1000)//1 sec delay
            }
        }, 0)

        button1.setOnClickListener {
            if (PosicionSpinner == 0){
                startActivity(Intent(this, AuthActivity::class.java))
            }
            if (PosicionSpinner == 1){
                startActivity(Intent(this, CreateBook::class.java))
            }

            if (PosicionSpinner == 2){
                startActivity(Intent(this, FilesAddActivity::class.java))
            }
            if (PosicionSpinner == 3){
                startActivity(Intent(this, ContentAddActivity::class.java))
            }
            if (PosicionSpinner == 4){
                startActivity(Intent(this, DeleteBook::class.java))
            }

        }

    }

    private fun actualizartextview(posicion:Int) {
        if(posicion == 0){

            editTextOpciones.text = "En este apartado puede agregar nuevos escritores o " +
                    "nuevos ususarios administradores los cuales administraran a los escritores  "
        }
        if(posicion == 1){
            editTextOpciones.text = "En este apartado puede crear un nuevo libro a la base de datos"
        }
        if(posicion == 2){
            editTextOpciones.text = ""
        }
        if(posicion == 3){
            editTextOpciones.text = "En este apartador puede agregar nuevas poesias a los libros anteriormente creados" +
                    "paraque los usuarios puedan disfrutar de este"
        }

    }

}