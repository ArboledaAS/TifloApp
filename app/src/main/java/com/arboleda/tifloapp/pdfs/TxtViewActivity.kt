package com.arboleda.tifloapp.pdfs

import android.net.MacAddress.fromBytes
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityTxtViewBinding
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.firebase.firestore.Blob.fromBytes
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TxtViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTxtViewBinding

    private companion object{
        const val TAG = "TXT_VIEW"
    }

    private var txturl = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTxtViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        txturl = intent.getStringExtra("txturl")!!


        cargarTxtUrl(txturl)

        binding.buttomregresar.setOnClickListener {
            onBackPressed()
        }


    }


    private fun cargarTxtUrl(pdfurl: String) {

        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfurl)
        reference.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener {bytes->
                Log.d(TAG,"cargarTxtUrl: Obteniendo Archivo de texto")

                println("AQUI ESTAN LOS BYTES DEL PDF"+bytes)

                val uri = String(bytes)

                binding.campodetexto.text = "${uri}"
                binding.progressBar.visibility = View.GONE


            }
            .addOnFailureListener {e->
                Log.d(TAG,"cargarTxtUrl: Fallo la carga del TXT debido a ${e.message}")
                binding.progressBar.visibility = View.GONE
            }
    }






}