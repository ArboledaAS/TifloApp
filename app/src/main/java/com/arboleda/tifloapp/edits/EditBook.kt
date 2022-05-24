package com.arboleda.tifloapp.edits

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityEditBookBinding
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_book.*
import kotlinx.android.synthetic.main.activity_first_user_list.*

class EditBook : AppCompatActivity() {

    private lateinit var binding: ActivityEditBookBinding

    private val SELEC_ACTIVITY_IMAGE =201
    private var imageUri: Uri? = null

    private companion object{
        private const val TAG = "BOOK_EDIT_TAG"
    }

    private var BookId = ""
    private var ImageUrl = ""

    private lateinit var progressDialog: ProgressDialog

    private lateinit var BookTitleArrayList: ArrayList<String>

    private lateinit var categoryIdArrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BookId = intent.getStringExtra("bookid")!!
        ImageUrl = intent.getStringExtra("imageurl")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere Porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        LoadCategories()
        LoadBookInfo()

        binding.buttomregresar.setOnClickListener {
            finish()
        }

        binding.guardarlibroButton.setOnClickListener {
            validateData()
        }

        binding.selececionarfotoButton.setOnClickListener {
            seleccionarfotodegaleria(this, SELEC_ACTIVITY_IMAGE)
        }

    }

    private fun LoadBookInfo() {
        Log.d(TAG,"LoadBookInfo: Cargando informacion")

        val ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.child(BookId)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val title = snapshot.child("name").value.toString()
                        val descripcion = snapshot.child("info").value.toString()
                        val palabraclave = snapshot.child("pclave").value.toString()
                        val imagen = snapshot.child("img").value.toString()

                        binding.edittextnombrelibro.setText(title)
                        binding.edittextdescripcionlibro.setText(descripcion)
                        binding.edittextpalabraclave.setText(palabraclave)
                        Glide.with(this@EditBook).load("$imagen").into(imageViewBook)


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }


    private var title = ""
    private var descripcion = ""
    private var pclave = ""

    private fun validateData() {
        title = binding.edittextnombrelibro.text.toString().trim()
        descripcion = binding.edittextdescripcionlibro.text.toString().trim()
        pclave = binding.edittextpalabraclave.text.toString().trim()

        if (title.isEmpty()){
            Toast.makeText(this, "Ingrese un titulo para el libro", Toast.LENGTH_LONG).show()
        }

        if (descripcion.isEmpty()){
            Toast.makeText(this, "Ingrese una descripcion para el libro", Toast.LENGTH_LONG).show()
        }

        if (pclave.isEmpty()){
            Toast.makeText(this, "Ingrese una palabra clave para el libro", Toast.LENGTH_LONG).show()
        }



        else{
            UpdateBook()
        }

    }

    private fun UpdateBook() {

        if (imageUri == null){

        Log.d(TAG,"UpdateBook: Iniciando actualizacion del libro...")

        progressDialog.setMessage("Actualizando Libro")
        progressDialog.show()

        val hashMap = HashMap<String, Any>()
        hashMap["name"] = "$title"
        hashMap["info"] = "$descripcion"
        hashMap["pclave"] = "$pclave"


        val ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.child(BookId)
                .updateChildren(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG,"UpdateBook: Finalizando actualizacion del libro...")

                    Toast.makeText(this, "Libro actualizado....", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener { e->
                    Log.d(TAG,"UpdateBook: Iniciando actualizacion del libro...")
                    progressDialog.dismiss()
                    Toast.makeText(this, "Fallo la actualizacion del libro debido a ${e.message}", Toast.LENGTH_LONG).show()

                }
        }

        else{

            var storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(ImageUrl)
            storageReference.delete()
                    .addOnSuccessListener {

                        progressDialog.show()
                        val  storageReference = FirebaseStorage.getInstance().getReference("imagenes/imagen_$BookId")


                        ////////////////
                        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                            val uriTask = taskSnapshot.storage.downloadUrl
                            while (!uriTask.isSuccessful);
                            val downloadUri = uriTask.result
                            if (uriTask.isSuccessful) {

                                /////////////////////////////
                                Log.d(TAG, "UpdateBook: Iniciando actualizacion del libro...")

                                progressDialog.setMessage("Actualizando Libro")
                                progressDialog.show()

                                val hashMap = HashMap<String, Any>()
                                hashMap["name"] = "$title"
                                hashMap["info"] = "$descripcion"
                                hashMap["pclave"] = "$pclave"
                                hashMap["img"] = "$downloadUri"


                                val ref = FirebaseDatabase.getInstance().getReference("libros")
                                ref.child(BookId)
                                        .updateChildren(hashMap)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "UpdateBook: Finalizando actualizacion del libro...")
                                            progressDialog.dismiss()
                                            Toast.makeText(this, "Libro actualizado....", Toast.LENGTH_LONG).show()

                                        }
                                        .addOnFailureListener { e ->
                                            Log.d(TAG, "UpdateBook: Iniciando actualizacion del libro...")
                                            progressDialog.dismiss()
                                            Toast.makeText(this, "Fallo la actualizacion del libro debido a ${e.message}", Toast.LENGTH_LONG).show()

                                        }
                                ///////////////////////////

                            }}




                    }.addOnFailureListener {
                        Toast.makeText(this,"No se pudo eliminar el libro de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                    }




        }

    }

    private fun LoadCategories() {
        Log.d(TAG,"LoadCategories: Loadin Categories")

        BookTitleArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                BookTitleArrayList.clear()

                for (ds in snapshot.children){
                    val id = "${ds.child("id").value}"

                    BookTitleArrayList.add(id)

                    Log.d(TAG, "onDataChange: Book Id $id")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    fun seleccionarfotodegaleria(activity: Activity, code: Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /// Verificar Seleccionar imagen
        when {
            requestCode == SELEC_ACTIVITY_IMAGE && resultCode == Activity.RESULT_OK ->{
                imageUri = data!!.data
                binding.imageViewBook.setImageURI(imageUri)
            } }

        /**
        //Verificar reconocimiento
        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            edittextpalabraclave.setText(deSpeechToText)

        }*/

    }






}