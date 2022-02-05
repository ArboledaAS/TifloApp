package com.arboleda.tifloapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.arboleda.tifloapp.adapter.AdapterDeleteBook
import com.arboleda.tifloapp.adapter.LibrosAdapter
import com.arboleda.tifloapp.databinding.ActivityMainBinding
import com.arboleda.tifloapp.databinding.RowUser2ListBinding
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menulibros.DeleteBook
import com.arboleda.tifloapp.menus.MasterMenu
import com.arboleda.tifloapp.model.LibroData
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.FirstUserListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //firebase
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var textToSpeech: TextToSpeech

    private lateinit var baseArrayList:ArrayList<ModelUniversal>

    private val TAG = "DATA_BASE"

    lateinit var mDatabase:DatabaseReference
    private lateinit var libroList:ArrayList<LibroData>
    private lateinit var mAdapter:LibrosAdapter

    private val RQ_ESCUCHA = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
       // loadFileBook()

        //TEXTO A VOZ
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
                textToSpeech.setSpeechRate(0.7F)
                textToSpeech.setPitch(1.0F)
            }
        })

        //INICIADOR
        libroList = ArrayList()
        mAdapter = LibrosAdapter(this,libroList)
        recyclerLibros.layoutManager = LinearLayoutManager(this)
        recyclerLibros.setHasFixedSize(true)
        recyclerLibros.adapter = mAdapter

        getLibrosData()

        binding.ReconocerBottom.setOnClickListener {
            if (textToSpeech.isSpeaking){
                textToSpeech.stop()
            }
            entradaDeVoz()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            editTextSpeech.text = deSpeechToText
            tomaDeOrdenes(deSpeechToText)


        }
    }

    fun entradaDeVoz(){
        /*
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "El reconocimiento no esta habilitado", Toast.LENGTH_SHORT).show()
        }else{

        }*/
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla Porfavor")
        startActivityForResult(i, RQ_ESCUCHA)
    }

    fun tomaDeOrdenes(decision: String){
        if (decision == "Libros" || decision == "Libro" || decision == "Lista" || decision == "Listas" ){
            verificarBase2()
        }
        else if (decision == "Ayuda"){
            textToSpeech.speak("Recuerda que hay comandos predeterminados para cada pantalla.\n" +
                    "También hay comandos universales que te sirven en cualquier pantalla los cuales son.\n" +
                    "la palabra. comandos. Para saber los comandos de una pantalla en específico.\n" +
                    "\n" +
                    " lista. para saber el nombre y palabra clave.\n" +
                    "\n" +
                    "Inicio. que te llevara a la pantalla principal.\n" +
                    "\n" +
                    "Atrás. para volver a la pantalla anterior si lo requieres.\n" +
                    "\n" +
                    "Y por último ayuda. que es la que mencionaste para saber de estos comandos.\n" +
                    "Espero que disfrutes de la aplicación \n ",TextToSpeech.QUEUE_FLUSH,null)
        }
        else if (decision == "Comandos" || decision == "Comando"){
            textToSpeech.speak("Estas en la pantalla principal. libros." +
                    "puedes decir Lista. o Libros. para saber que libros hay disponibles y su palabra clave ",TextToSpeech.QUEUE_FLUSH,null)
        }
        else if (decision == "Inicio" || decision == "Atrás"){
            textToSpeech.speak("Ya te encuentras en la pantalla principal de la aplicación ",
                    TextToSpeech.QUEUE_FLUSH,null)
        }
        else{
            verificarBase(decision)
        }

    }


    /*private fun loadFileBook() {
        Log.d(TAG, "Cargando Archivos LIBRO")

        baseArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpiar lista despues de agregar datos
                baseArrayList.clear()
                for (ds in snapshot.children){

                    val model = ds.getValue(ModelUniversal::class.java)

                    baseArrayList.add(model!!)
                    Log.d(TAG,"onDataChange: ${model.name}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }*/




    fun verificarBase(identificar:String){

        val ref = FirebaseDatabase.getInstance().getReference().child("libros")
        val buscar = ref.orderByChild("name").equalTo(identificar)
        buscar.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val book = snapshot.children.first().getValue<ModelUniversal>()
                    //Toast.makeText(this@MainActivity, "ESTE LIBRO EXISTE", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, FirstUserListActivity::class.java)
                    intent.putExtra("bookId", "${book?.id}")
                    intent.putExtra("bookname", "${book?.name}")
                    intent.putExtra("bookinfo", "${book?.info}")
                    startActivity(intent)

                }else{
                    textToSpeech.speak("El comando o libro que acabas de decir no existe",TextToSpeech.QUEUE_FLUSH,null)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun verificarBase2(){
        val ref = FirebaseDatabase.getInstance().getReference().child("libros")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var array = arrayListOf<String>()
                for (recorrido in snapshot.children){
                    val model = recorrido.getValue<ModelUniversal>()
                    array.add(model?.name.toString())
                    /*array.add(model!!.name)
                    array.add("palabra clave: ${model.info}")*/
                }
                println(array)
                textToSpeech.speak("los libros disponibles son: $array",TextToSpeech.QUEUE_FLUSH,null)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }





    private fun getLibrosData() {
        mDatabase =FirebaseDatabase.getInstance().getReference("libros")
        mDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (libroSnapshot in snapshot.children){
                        val libro = libroSnapshot.getValue(LibroData::class.java)
                        libroList.add(libro!!)
                    }
                    recyclerLibros.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,error.message,Toast.LENGTH_SHORT).show()
            }

        })
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
            R.id.menu_lector -> startActivity(Intent(this,MasterMenu::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    ////////////////inicializa el menu escritor y lector

}