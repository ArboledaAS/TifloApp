package com.arboleda.tifloapp.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.arboleda.tifloapp.MainActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivitySecondUserListBinding
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.pdfs.PdfViewActivity
import com.arboleda.tifloapp.pdfs.TxtViewActivity
import com.arboleda.tifloapp.view.adapterview.AdapterFirstUserList
import com.arboleda.tifloapp.view.adapterview.AdapterSecondUserList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second_user_list.*
import kotlinx.android.synthetic.main.activity_second_user_list.editTextSpeech
import java.util.*
import kotlin.collections.ArrayList

class SecondUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondUserListBinding

    private companion object{
        const val TAG = "FILE_LIST_ADMIN_TAG"
    }

    private lateinit var fileArrayList: ArrayList<ModelUniversal>

    private lateinit var adapterFileAdmin: AdapterSecondUserList

    //Text to Speech y Speech to text
    lateinit var textToSpeech: TextToSpeech
    private val RQ_ESCUCHA = 102

    private  var poesiaid = ""
    private  var bookId = ""
    private var poesianame = ""
    private var pclavepoesia = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_second_user_list)
        binding = ActivitySecondUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        poesiaid = intent.getStringExtra("poesiaid")!!
        bookId = intent.getStringExtra("bookid")!!
        poesianame = intent.getStringExtra("poesianame")!!
        pclavepoesia = intent.getStringExtra("pclavepoesia")!!


        loadFileList()

        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
                textToSpeech.setSpeechRate(0.7F)
                textToSpeech.setPitch(1.0F)
            }
        })


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

    fun tomaDeOrdenes(decision: String) {
        if (decision == "Atrás"){
            finish()
        }
        else if (decision == "Inicio"){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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
                    "Espero que disfrutes de la aplicación \n ", TextToSpeech.QUEUE_FLUSH,null)
        }
        else if (decision == "Archivo"|| decision == "Archivos" || decision == "Lista" || decision == "Listas"){
            verificarBase2()
        }else if (decision == "Comandos" || decision == "Comando"){
            textToSpeech.speak("Estas en la pantalla Archivos. Dentro de la poesia $poesianame." +
                    "puedes decir Lista. o Archivos. para saber las palabras claves de los archivos disponibles ",TextToSpeech.QUEUE_FLUSH,null)
        }
        else{
            verificarBase3(decision)
        }
    }


    fun verificarBase(identificar:String){
        val ref = FirebaseDatabase.getInstance().getReference("Archivos").child(poesiaid)
        ref.orderByChild("poesiaid").equalTo(poesiaid)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {


                        if (snapshot.exists())
                            for (ds in snapshot.children){

                                val model = ds.getValue(ModelUniversal::class.java)


                                if (model != null && model.pclave == identificar) {

                                    if (model.tipo == "0"){
                                        val intent = Intent(this@SecondUserListActivity, FinalActivity::class.java)
                                        intent.putExtra("poesiaurl", model.url)
                                        startActivity(intent)
                                    }
                                    else if (model.tipo == "1"){
                                        val intent = Intent(this@SecondUserListActivity, PdfViewActivity::class.java)
                                        intent.putExtra("pdfurl", model.url)
                                        startActivity(intent)
                                    }
                                    else if (model.tipo == "2"){
                                        val intent = Intent(this@SecondUserListActivity, TxtViewActivity::class.java)
                                        intent.putExtra("txturl", model.url)
                                        startActivity(intent)
                                    }


                                }
                            }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SecondUserListActivity,error.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }



    fun  verificarBase2(){
        val ref = FirebaseDatabase.getInstance().getReference("Archivos").child(poesiaid)
        ref.orderByChild("poesiaid").equalTo(poesiaid)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var array = arrayListOf<String>()
                        for (ds in snapshot.children){

                            val model = ds.getValue(ModelUniversal::class.java)

                            if (model != null) {
                                array.add(model.pclave)
                            }
                        }

                        textToSpeech.speak("Los Archivos disponibles en la poesia. $poesianame son. ${array}.",TextToSpeech.QUEUE_FLUSH,null)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SecondUserListActivity,error.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }


    fun  verificarBase3(identificar:String) {
        val ref = FirebaseDatabase.getInstance().getReference("Archivos").child(poesiaid)
        ref.orderByChild("pclave").equalTo(identificar)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            verificarBase(identificar)
                        }else{
                            textToSpeech.speak("El comando o Archivo que acabas de decir no existe",TextToSpeech.QUEUE_FLUSH,null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@SecondUserListActivity,error.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }


    

    private fun loadFileList() {
        fileArrayList = ArrayList()
        /*val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.orderByChild("librosid").equalTo(bookId)*/

        val ref = FirebaseDatabase.getInstance().getReference("Archivos").child(poesiaid)
        ref.orderByChild("poesiaid").equalTo(poesiaid)
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
                        adapterFileAdmin = AdapterSecondUserList(this@SecondUserListActivity, fileArrayList)
                        binding.recycleruser2.adapter = adapterFileAdmin
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }

}