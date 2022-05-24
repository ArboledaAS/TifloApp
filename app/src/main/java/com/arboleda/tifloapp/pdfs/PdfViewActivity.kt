package com.arboleda.tifloapp.pdfs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.View
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityNewBinding
import com.arboleda.tifloapp.databinding.ActivityPdfViewBinding
import com.arboleda.tifloapp.pdfs.Constants.MAX_BYTES_PDF
import com.google.firebase.storage.FirebaseStorage

class PdfViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewBinding

    private companion object{
        const val TAG = "PDF_VIEW"
    }

    var pdfurl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfurl = intent.getStringExtra("pdfurl")!!


        cargarPdfUrl(pdfurl)

        binding.buttomregresar.setOnClickListener {
            onBackPressed()
        }


    }

    private fun cargarPdfUrl(pdfurl: String) {

        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfurl)
        reference.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener {bytes->
                Log.d(TAG,"cargarPdfUrl: Obteniendo pdf")

                binding.pdfView.fromBytes(bytes)
                    .swipeHorizontal(false)
                    .onPageChange{page, pageCount->

                        val currentPage = page+1
                        binding.toolbarSubtittleTv.text = "$currentPage/$pageCount"

                    }
                    .onError {t->
                        Log.d(TAG, "cargarPdfUrl: ${t.message}")
                    }
                    .onPageError { page, t ->
                        Log.d(TAG, "cargarPdfUrl: ${t.message}")
                    }
                    .load()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {e->
                Log.d(TAG,"cargarPdfUrl: Fallo la carga del pdf debido a ${e.message}")
                binding.progressBar.visibility = View.GONE

            }
    }


}