package com.arboleda.tifloapp.pdfs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityPdfViewBinding
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_pdf_view.*
import java.util.*


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


        /**

                binding.webView.webChromeClient = object : WebChromeClient(){}

        binding.webView.webViewClient = object : WebViewClient(){}

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true
        settings.supportZoom()


        binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfurl)
        println(pdfurl)

*/

    }


    private fun cargarPdfUrl(pdfurl: String) {

        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfurl)
        reference.getBytes(Constants.MAX_BYTES_PDF)
            .addOnSuccessListener {bytes->
                Log.d(TAG,"cargarPdfUrl: Obteniendo pdf")

                println("AQUI ESTAN LOS BYTES DEL PDF"+bytes)

                binding.pdfView.fromBytes(bytes)
                        .defaultPage(0)
                        .pageFitPolicy(FitPolicy.BOTH)
                        .spacing(10)
                        .enableAnnotationRendering(true)
                        .linkHandler( DefaultLinkHandler(binding.pdfView))



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