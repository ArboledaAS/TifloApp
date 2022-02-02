
package com.arboleda.tifloapp.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.adapter.AdapterFileAdmin
import com.arboleda.tifloapp.databinding.ActivityFirstUserListBinding
import com.arboleda.tifloapp.menulibros.FileListAdminActivity
import com.arboleda.tifloapp.model.ModelFile
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.adapterview.AdapterFirstUserList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class FirstUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstUserListBinding

    private companion object{
        const val TAG = "FILE_LIST_ADMIN_TAG"
    }

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
        bookId = intent.getStringExtra("bookId")!!
        bookname = intent.getStringExtra("bookname")!!
        bookinfo = intent.getStringExtra("bookinfo")!!

        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
            }
        })

        loadFileList()

        binding.ReconocerBottom.setOnClickListener {
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
        if (decision == "Poesía"|| decision == "Poesías"){
            verificarBase2()
        }else{
            verificarBase(decision)
        }

    }


    fun verificarBase(identificar:String){
        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.orderByChild("librosid").equalTo(bookId)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var array = arrayListOf<String>()
                        println("asodASDUHGASHGD $snapshot")
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
                        TODO("Not yet implemented")
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
                                array.add("palabra clave ${model.pclave}")
                            }
                        }
                        println(array)
                        textToSpeech.speak("Las poesias disponibles son: $array",TextToSpeech.QUEUE_FLUSH,null)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
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
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}