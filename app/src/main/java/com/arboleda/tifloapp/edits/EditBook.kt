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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToInt

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
    private val RQ_ESCUCHA = 102
    private var textprogreso = 0.0

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

        binding.reconocimientoButton.setOnClickListener {
            entradaDeVoz()
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
        var palabraclave = edittextpalabraclave.text.toString().capitalize()

        if (title.isEmpty()){
            Toast.makeText(this, "Ingrese un titulo para el libro", Toast.LENGTH_LONG).show()
            progressDialog.dismiss()
        }

        else if (descripcion.isEmpty()){
            Toast.makeText(this, "Ingrese una descripcion para el libro", Toast.LENGTH_LONG).show()
            progressDialog.dismiss()
        }

        else if (pclave.isEmpty()){
            Toast.makeText(this, "Ingrese una palabra clave para el libro", Toast.LENGTH_LONG).show()
            progressDialog.dismiss()
        }
        else if (palabraclave == "Libros" || palabraclave == "Libro" || palabraclave == "Lista" ||
            palabraclave == "Listas" || palabraclave == "Ayuda" || palabraclave == "Comandos"
            || palabraclave == "Comando" || palabraclave == "Inicio" || palabraclave == "Atrás" ||
            palabraclave == "Actualiazar" || palabraclave == "Inicio" || palabraclave == "Inicio"
            || palabraclave == "Inicio" || palabraclave == "Reproducir"|| palabraclave == "Play"
            || palabraclave == "Reproduce" || palabraclave == "Repetir" ||
            palabraclave == "Reiniciar"){
            progressDialog.dismiss()
            Toast.makeText(this,"Esta palabra esta reservada para el Sistema",Toast.LENGTH_LONG).show()
        }



        else{
            verificarBase(palabraclave)

        }

    }

    private fun UpdateBook() {

        if (imageUri == null){

        Log.d(TAG,"UpdateBook: Iniciando actualizacion del libro...")

        progressDialog.setMessage("Actualizando Libro: ${textprogreso}%")
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
                    progressDialog.dismiss()

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

                            }}.addOnProgressListener {
                            textprogreso = (100.0 * it.bytesTransferred) / it.totalByteCount
                            progressDialog.setMessage("Creando Libro: ${textprogreso.roundToInt()}%")
                        }




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


        //Verificar reconocimiento
        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            binding.edittextpalabraclave.setText(deSpeechToText)

        }

    }

    fun entradaDeVoz(){
        /*
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "El reconocimiento no esta habilitado", Toast.LENGTH_SHORT).show()
        }else{

        }*/
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla Porfavor")
        startActivityForResult(i, RQ_ESCUCHA)
    }



    fun verificarBase(identificar:String){

        val ref = FirebaseDatabase.getInstance().reference.child("libros")
        val buscar = ref.orderByChild("pclave").equalTo(identificar)
        buscar.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    progressDialog.dismiss()
                    Toast.makeText(this@EditBook,"Esta palabra clave ya está asignada a un libro",Toast.LENGTH_LONG).show()
                }else{
                    UpdateBook()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                Toast.makeText(this@EditBook,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }






}