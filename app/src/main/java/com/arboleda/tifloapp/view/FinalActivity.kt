package com.arboleda.tifloapp.view

import android.accessibilityservice.AccessibilityService
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updateMarginsRelative
import com.arboleda.tifloapp.LoginActivity
import com.arboleda.tifloapp.MainActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityFinalBinding
import com.arboleda.tifloapp.menulibros.DeleteBook
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.activity_final.*
import kotlinx.android.synthetic.main.activity_final.ReconocerBottom
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class FinalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinalBinding

    var isFullScreen = false
    lateinit var simpleExoPlayer:SimpleExoPlayer
    private lateinit var constraintLayoutRoot: ConstraintLayout
    private lateinit var playerView:PlayerView

    val RQ_ESCUCHA = 102
    lateinit var textToSpeech: TextToSpeech
    private lateinit var videoView:VideoView

    private var Url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        playerView = findViewById<PlayerView>(R.id.playerexo)
        val progressBar = findViewById<ProgressBar>(R.id.progress_Bar)
        val bt_fullscreen = findViewById<ImageView>(R.id.exo_fullscreen)
        val bt_replay = findViewById<ImageView>(R.id.exo_replay)

        binding.buttomregresar.setOnClickListener {
            onBackPressed()
        }




        bt_fullscreen.setOnClickListener {

            if (!isFullScreen){
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_round_fullscreen))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
/**
                val layoutParams = playerexo.layoutParams as RelativeLayout.LayoutParams
                layoutParams.setMargins(0, 0, 0, 0)
                playerexo.layoutParams = layoutParams
*/

                playInFullscreen(enable = false)

            }
            else{
                bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_round_fullscreen_exit))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)



                playInFullscreen(enable = true)
            }

            isFullScreen =! isFullScreen
        }

        bt_replay.setOnClickListener {
            simpleExoPlayer.seekTo(0)
            simpleExoPlayer.play()
        }



        simpleExoPlayer= SimpleExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        playerView.player = simpleExoPlayer
        playerView.keepScreenOn = true
        simpleExoPlayer.addListener(object: Player.Listener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING)
                {
                    progressBar.visibility = View.VISIBLE
                }else if (playbackState == Player.STATE_READY){
                    progressBar.visibility = View.GONE
                }
            }

        })




        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {status ->
            if (status != TextToSpeech.ERROR){
                textToSpeech.language = Locale.getDefault()
                textToSpeech.setSpeechRate(0.7F)
                textToSpeech.setPitch(1.0F)
            }
        })


        val intent = intent
        Url = intent.getStringExtra("poesiaurl")!!

        val videoSource = Uri.parse("$Url")
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()



        /**videoView = findViewById<VideoView>(R.id.Reproductor)
        val mediaController = MediaController(this)
        //establecer vista de control
        mediaController.setAnchorView(videoView)
        //Controles manuales
        videoView.setMediaController(mediaController)
        videoView.setVideoPath("$Url")
        videoView.requestFocus()
        videoView.start()*/

        ReconocerBottom.setOnClickListener {
            if (textToSpeech.isSpeaking){
                textToSpeech.stop()
            }
/**
            videoView.pause()*/
            entradaDeVoz()
            simpleExoPlayer.pause()
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

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
    }


    /**
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val constraintSet = ConstraintSet()
        constraintSet.connect(playerView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0 )
        constraintSet.connect(playerView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0 )
        constraintSet.connect(playerView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0 )
        constraintSet.connect(playerView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END,0 )



        
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
        }
        else{
            showSystemUI()

            val layoutParams = playerexo.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"


        }
        window.decorView.requestLayout()
    }*/

    private fun hideSystemUI(){
        actionBar?.hide()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    private fun showSystemUI(){
        actionBar?.show()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }


    private fun tomaDeOrdenes(decision:String) {
        if (decision == "Reproducir"|| decision == "Play" || decision == "Reproduce"){
            simpleExoPlayer.play()
        }
        else if (decision == "Repetir" || decision == "Reiniciar"){
            simpleExoPlayer.seekTo(0)
            simpleExoPlayer.play()
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



    private fun playInFullscreen(enable: Boolean){
        if(enable){
            playerexo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            simpleExoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

        }else{
            playerexo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            simpleExoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT

        }
    }


}