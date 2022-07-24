package com.arboleda.tifloapp.pdfs

import android.app.Activity
import android.content.Intent
import android.net.MacAddress.fromBytes
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import com.arboleda.tifloapp.MainActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityTxtViewBinding
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.firebase.firestore.Blob.fromBytes
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class TxtViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTxtViewBinding

    private companion object{
        const val TAG = "TXT_VIEW"
    }

    private var txturl = ""

    private var ContenedorUri = ""

    val RQ_ESCUCHA = 102
    lateinit var textToSpeech: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTxtViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txturl = intent.getStringExtra("txturl")!!


        cargarTxtUrl(txturl)


        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
                textToSpeech.setSpeechRate(0.7F)
                textToSpeech.setPitch(1.0F)
            }
        })




        binding.buttomregresar.setOnClickListener {
            if (textToSpeech.isSpeaking){
                textToSpeech.stop()
            }
            
            onBackPressed()

        }


        binding.ReconocerBottom.setOnClickListener {

            if (textToSpeech.isSpeaking){
                textToSpeech.stop()
            }

            entradaDeVoz()
        }


    }


    private fun cargarTxtUrl(pdfurl: String) {

        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfurl)
        reference.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener {bytes->
                Log.d(TAG,"cargarTxtUrl: Obteniendo Archivo de texto")

                println("AQUI ESTAN LOS BYTES DEL PDF"+bytes)

                ContenedorUri = String(bytes)

                binding.campodetexto.text = "$ContenedorUri"
                binding.progressBar.visibility = View.GONE


            }
            .addOnFailureListener {e->
                Log.d(TAG,"cargarTxtUrl: Fallo la carga del TXT debido a ${e.message}")
                binding.progressBar.visibility = View.GONE
            }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            //editTextSpeech.text = deSpeechToText
            tomaDeOrdenes(deSpeechToText)


        }
    }


    private fun entradaDeVoz() {

        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla Porfavor")
        startActivityForResult(i, RQ_ESCUCHA)
    }



    private fun tomaDeOrdenes(decision:String) {
        if (decision == "Leer"){
            textToSpeech.speak("$ContenedorUri", TextToSpeech.QUEUE_FLUSH, null)
        }

        else if (decision == "Inicio"){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else if (decision == "Ayuda"){
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
            textToSpeech.speak("Estas en la pantalla Final." +
                    "puedes decir Reproducir. Pausar o Repetir para manejar el video. ",TextToSpeech.QUEUE_FLUSH,null)
        }

        else{
            textToSpeech.speak("El comando que acabas de decir no existe",
                    TextToSpeech.QUEUE_FLUSH,null)
        }

    }


}