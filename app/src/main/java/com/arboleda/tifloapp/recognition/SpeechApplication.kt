package com.arboleda.tifloapp.recognition

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import java.util.*

class SpeechApplication {

/*

    val RQ_ESCUCHA = 102
    lateinit var textToSpeech: TextToSpeech

    textToSpeech = TextToSpeech(this,this)

    //////////////////////////////////////////////////////////////////////////// RECONOCIMIENTO
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)


            var contenedor = result?.get(0).toString()


        }
    }

    fun entradaDeVoz(){

        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "El reconocimiento no esta habilitado", Toast.LENGTH_SHORT).show()
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla Porfavor")
            startActivityForResult(i, RQ_ESCUCHA)
        }
    }
    //////////////////////////////////////////////////////////////////////////// RECONOCIMIENTO

    /////////////////////////////////////////////// Funcion de la voz (Hablar)
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val res: Int = textToSpeech.setLanguage(Locale.ITALY)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"LENGUAJE NO SOPORTADO", Toast.LENGTH_LONG).show()
            }else{Toast.makeText(this,"FALLO EL INICIO",Toast.LENGTH_LONG).show()}
        }
    }
    */
}