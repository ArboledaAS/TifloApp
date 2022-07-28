
package com.arboleda.tifloapp.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.adapter.AdapterFileAdmin
import com.arboleda.tifloapp.databinding.ActivityFirstUserListBinding
import com.arboleda.tifloapp.menulibros.FileListAdminActivity
import com.arboleda.tifloapp.model.ModelFile
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.adapterview.AdapterFirstUserList
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_first_user_list.*
import kotlinx.android.synthetic.main.activity_main.editTextSpeech

class FirstUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstUserListBinding

    private companion object{
        const val TAG = "FILE_LIST_ADMIN_TAG"
    }

    private  var bookimg = ""
    private  var bookId = ""
    private var bookname = ""
    private var bookinfo = ""

    private lateinit var fileArrayList: ArrayList<ModelUniversal>

    private lateinit var adapterFileAdmin: AdapterFirstUserList

    lateinit var textToSpeech: TextToSpeech
    private val RQ_ESCUCHA = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_first_user_list)
        binding = ActivityFirstUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        bookimg = intent.getStringExtra("imgdellibro")!!
        bookId = intent.getStringExtra("bookId")!!
        bookname = intent.getStringExtra("bookname")!!
        bookinfo = intent.getStringExtra("bookinfo")!!

        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
                textToSpeech.setSpeechRate(0.7F)
                textToSpeech.setPitch(1.0F)
            }
        })

        loadFileList()

        binding.ReconocerBottom.setOnClickListener {
            if (textToSpeech.isSpeaking){
                textToSpeech.stop()
            }

            entradaDeVoz()
        }

        /*binding.carviewpantalla.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("$bookname")
            builder.setMessage("${bookinfo}")
            builder.setPositiveButton("Cerrar",null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }*/

        binding.descripcionbutton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("$bookname")
            builder.setMessage("${bookinfo}")
            builder.setPositiveButton("Cerrar descripción",null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        Glide.with(this).load("$bookimg").into(libroImg2)
        binding.libroName2.setText("$bookname")
        binding.libroInfo2.setText("$bookinfo")



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
        if (decision == "Poesía"|| decision == "Poesías" || decision == "Lista" || decision == "Listas"){
            verificarBase2()
        }
        else if (decision == "Descripción"){
            textToSpeech.speak("$bookinfo",TextToSpeech.QUEUE_FLUSH,null)
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
        else if (decision == "Inicio" || decision == "Atrás"){
            finish()
        }else if (decision == "Comandos" || decision == "Comando"){
            textToSpeech.speak("Estas en la pantalla Poesia. Dentro del libro. $bookname" +
                    "puedes decir Lista. o Poesias. para saber las poesias que hay disponibles y sus palabra clave." +
                    "Tambien puedes decir la palabra. Descripción. para saber la Descripción del libro $bookname  ",TextToSpeech.QUEUE_FLUSH,null)
        }

        else{
            //verificarBase(decision)
            verificarBase3(decision)
        }

    }


    fun verificarBase(identificar:String){
        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.orderByChild("librosid").equalTo(bookId)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        //println("asodASDUHGASHGD $snapshot")
                        if (snapshot.exists())
                        for (ds in snapshot.children){

                            val model = ds.getValue(ModelUniversal::class.java)


                            if (model != null && model.pclave == identificar) {

                                /*
                                array.add(model.id)
                                array.add(model.librosid)
                                array.add(model.name)
                                array.add(model.pclave)*/
                                val intent = Intent(this@FirstUserListActivity, SecondUserListActivity::class.java)
                                intent.putExtra("poesiaid", model.id)
                                intent.putExtra("bookid", model.librosid)
                                intent.putExtra("poesianame", model.name)
                                intent.putExtra("pclavepoesia", model.pclave)
                                startActivity(intent)
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@FirstUserListActivity,error.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }


    fun  verificarBase2(){
        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.orderByChild("librosid").equalTo(bookId)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var array = arrayListOf<String>()
                        for (ds in snapshot.children){

                            val model = ds.getValue(ModelUniversal::class.java)

                            if (model != null) {
                                array.add(model.name)
                                array.add("palabra clave. ${model.pclave}.")
                            }
                        }
                        println(array)
                        textToSpeech.speak("Las poesias disponibles son. ${array}.",TextToSpeech.QUEUE_FLUSH,null)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@FirstUserListActivity,error.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }

    //VERIFICA QUE EXISTA LA PALABRA Y EN CASO TAL MANDARLE LA PALABRA A LA FUNCION verificarBase()
    fun  verificarBase3(identificar:String) {
        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.orderByChild("pclave").equalTo(identificar)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            verificarBase(identificar)
                        }else{
                            textToSpeech.speak("El comando o Poesia que acabas de decir no existe",TextToSpeech.QUEUE_FLUSH,null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@FirstUserListActivity,error.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun loadFileList() {
        fileArrayList = ArrayList()
        /*val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.orderByChild("librosid").equalTo(bookId)*/

        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.orderByChild("librosid").equalTo(bookId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    fileArrayList.clear()
                    for (ds in snapshot.children){

                        val model = ds.getValue(ModelUniversal::class.java)


                        if (model != null) {
                            fileArrayList.add(model)
                            Log.d(TAG,"onDataChange: ${model.name} ${model.id}")
                        }
                    }
                    adapterFileAdmin = AdapterFirstUserList(this@FirstUserListActivity, fileArrayList)
                    binding.recycleruser1.adapter = adapterFileAdmin
                    binding.viewLoading.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    ////////////////inicializa el menu escritor y lector

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu3, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //R.id.menu_escritor -> startActivity(Intent(this,CreateBook::class.java))
            R.id.volver -> {
                if (textToSpeech.isSpeaking){
                    textToSpeech.stop()
                }
                onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    ////////////////inicializa el menu escritor y lector*******




}