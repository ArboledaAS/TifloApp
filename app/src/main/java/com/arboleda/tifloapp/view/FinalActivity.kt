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
import com.arboleda.tifloapp.R
import kotlinx.android.synthetic.main.activity_final.*
import kotlinx.android.synthetic.main.activity_final.ReconocerBottom
import kotlinx.android.synthetic.main.activity_final.editTextSpeech
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class FinalActivity : AppCompatActivity() {


    val RQ_ESCUCHA = 102

    private var Url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)


        val intent = intent
        Url = intent.getStringExtra("poesiaurl")!!



        val videoView = findViewById<VideoView>(R.id.Reproductor)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoPath("$Url")
        videoView.requestFocus()
        videoView.start()

        ReconocerBottom.setOnClickListener {
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

    private fun tomaDeOrdenes(desicion:String) {
        if (desicion == "Reproducir"|| desicion == "Play"){

        }
    }
}