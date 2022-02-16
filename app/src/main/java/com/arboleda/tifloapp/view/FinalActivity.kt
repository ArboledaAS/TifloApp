package com.arboleda.tifloapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.arboleda.tifloapp.LoginActivity
import com.arboleda.tifloapp.MainActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.menulibros.DeleteBook
import kotlinx.android.synthetic.main.activity_final.*
import kotlinx.android.synthetic.main.activity_final.ReconocerBottom
import kotlinx.android.synthetic.main.activity_final.editTextSpeech
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class FinalActivity : AppCompatActivity() {


    val RQ_ESCUCHA = 102
    lateinit var textToSpeech: TextToSpeech
    private lateinit var videoView:VideoView

    private var Url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)

        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
                textToSpeech.setSpeechRate(0.7F)
                textToSpeech.setPitch(1.0F)
            }
        })


        val intent = intent
        Url = intent.getStringExtra("poesiaurl")!!



        videoView = findViewById<VideoView>(R.id.Reproductor)
        val mediaController = MediaController(this)
        //establecer vista de control
        mediaController.setAnchorView(videoView)
        //Controles manuales
        videoView.setMediaController(mediaController)
        videoView.setVideoPath("$Url")
        videoView.requestFocus()
        videoView.start()

        ReconocerBottom.setOnClickListener {
            if (textToSpeech.isSpeaking){
                textToSpeech.stop()
            }

            videoView.pause()
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



    private fun entradaDeVoz() {

                val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla Porfavor")
                startActivityForResult(i, RQ_ESCUCHA)
    }

    private fun tomaDeOrdenes(decision:String) {
        if (decision == "Reproducir"|| decision == "Play" || decision == "Reproduce"){
            videoView.start()
        }else if (decision == "Repetir" || decision == "Reiniciar"){
            videoView.stopPlayback()
            videoView.setVideoPath("$Url")
            videoView.start()
        }
        else if (decision == "Atrás"){
            finish()
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