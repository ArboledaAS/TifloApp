package com.arboleda.tifloapp.menulibros

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arboleda.tifloapp.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_book.*

class CreateBook : AppCompatActivity() {
    ///////////////////////
    private val db = FirebaseFirestore.getInstance()
    private val SELEC_ACTIVITY_IMAGE =201
    var libroname:String = ""
    private var imageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    //////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)



        guardarlibroButton.setOnClickListener {
            /*
            if(edittextnombrelibro.text.toString().isNotEmpty()){
                libroname = edittextnombrelibro.text.toString()
                var idlibro = System.currentTimeMillis().toString()
                db.collection("libros").document(idlibro)
                        .set(hashMapOf(
                                "id" to "$idlibro",
                                "nombre" to "$libroname",
                                "audio" to "",
                                "video" to "",
                                "imagen" to "")
                        )

            }else{
                Toast.makeText(this,"Ingrese texto en el campo y seleccione imagen", Toast.LENGTH_SHORT).show()}
*/          progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Espere Porfavor")
            progressDialog.setMessage("Creando Libro")
            progressDialog.setCanceledOnTouchOutside(false)
            var idlibro = System.currentTimeMillis().toString()

            if (edittextnombrelibro.text.toString().isEmpty()){
                Toast.makeText(this,"Nombre del libro es requerido", Toast.LENGTH_SHORT).show()
            }else if (imageUri == null){
                Toast.makeText(this,"Elige una imagen porfavor", Toast.LENGTH_SHORT).show()
            }else {
                subirimagenylibro(idlibro)
            }
        }

        selececionarfotoButton.setOnClickListener {
            seleccionarfotodegaleria(this, SELEC_ACTIVITY_IMAGE)
        }
    }

    fun seleccionarfotodegaleria(activity: Activity, code: Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == SELEC_ACTIVITY_IMAGE && resultCode == Activity.RESULT_OK ->{
                imageUri = data!!.data
                imageViewBook.setImageURI(imageUri)


            } } }

    fun subirimagenylibro(id:String){
        progressDialog.show()
        val  storageReference = FirebaseStorage.getInstance().getReference("imagenes/$id/imagen_$id")
/*
        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val downloadUri = uriTask.result
            if (uriTask.isSuccessful){
                libroname = edittextnombrelibro.text.toString()
                db.collection("libros").document(id)
                        .set(hashMapOf(
                                "id" to "$id",
                                "nombre" to "$libroname",
                                "audio" to "",
                                "video" to "",
                                "videolink" to "$downloadUri",
                                "imagen" to ""))
                        .addOnSuccessListener {taskSnapshot ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"Libro Creado",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {e ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

                        }

            }
        }
        */

        ////////////////
        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val downloadUri = uriTask.result
            if (uriTask.isSuccessful){
                libroname = edittextnombrelibro.text.toString()
                var librodescripcion =  edittextdescripcionlibro.text.toString()
                val dbReference = FirebaseDatabase.getInstance().getReference("libros")
                dbReference.child(id).setValue(hashMapOf(
                                "id" to "$id",
                                "name" to "$libroname",
                                "info" to "$librodescripcion",
                                "img" to "$downloadUri",
                                ))
                        .addOnSuccessListener {taskSnapshot ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"Libro Creado",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {e ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

                        }

            }
        }




        //////////////////////


    }

}