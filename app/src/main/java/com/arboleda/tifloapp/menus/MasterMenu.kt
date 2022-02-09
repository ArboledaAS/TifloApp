package com.arboleda.tifloapp.menus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arboleda.tifloapp.AuthActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.remove.RemoveUserActivity
import com.arboleda.tifloapp.menulibros.ContentAddActivity
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menulibros.DeleteBook
import com.arboleda.tifloapp.menulibros.PoesiaAddActivity
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
        /*
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

        }*/
/*
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                actualizartextview(PosicionSpinner)
                handler.postDelayed(this, 1000)//1 sec delay
            }
        }, 0)*/

        agregarUsuarioCarview.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        eliminarUsuarioCarview.setOnClickListener {
            startActivity(Intent(this, RemoveUserActivity::class.java))
        }

        crearLibroCarview.setOnClickListener {
            startActivity(Intent(this, CreateBook::class.java))
        }

        agregarPoesiaCarview.setOnClickListener {
            startActivity(Intent(this, PoesiaAddActivity::class.java))
        }

        agregarArchivosCarview.setOnClickListener {
            startActivity(Intent(this, ContentAddActivity::class.java))
        }

        eliminarContenidoCarview.setOnClickListener {
            startActivity(Intent(this, DeleteBook::class.java))
        }


    }



}