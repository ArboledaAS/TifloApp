package com.arboleda.tifloapp.menulibros

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.FirstUserListActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_book.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class CreateBook : AppCompatActivity() {
    ///////////////////////
    private val db = FirebaseFirestore.getInstance()
    private val SELEC_ACTIVITY_IMAGE =201
    var libroname:String = ""
    private var imageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    //////////////////////

    private val RQ_ESCUCHA = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)



        guardarlibroButton.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Espere Porfavor")
            progressDialog.setMessage("Creando Libro")
            progressDialog.setCanceledOnTouchOutside(false)

            var palabraclave = edittextpalabraclave.text.toString().capitalize()

            if (edittextnombrelibro.text.toString().isEmpty()){
                Toast.makeText(this,"Nombre del libro es requerido", Toast.LENGTH_LONG).show()
            }else if (edittextpalabraclave.text.toString().isEmpty()){
                Toast.makeText(this,"Es requerido una palabra clave para reconocer el libro", Toast.LENGTH_LONG).show()
            }
            else if (imageUri == null){
                Toast.makeText(this,"Elige una imagen porfavor", Toast.LENGTH_LONG).show()
            }else if (palabraclave == "Libros" || palabraclave == "Libro" || palabraclave == "Lista" || palabraclave == "Listas"
                    || palabraclave == "Ayuda" || palabraclave == "Comandos" || palabraclave == "Comando"
                    || palabraclave == "Inicio" || palabraclave == "Atrás" || palabraclave == "Actualiazar"
                    || palabraclave == "Inicio" || palabraclave == "Inicio" || palabraclave == "Inicio"){

                Toast.makeText(this,"Esta palabra esta reservada para el Sistema",Toast.LENGTH_LONG).show()
            }
            else {
                verificarBase(palabraclave)
            }
        }

        selececionarfotoButton.setOnClickListener {
            seleccionarfotodegaleria(this, SELEC_ACTIVITY_IMAGE)
        }


        reconocimientoButton.setOnClickListener {
            entradaDeVoz()
        }

    }

    fun seleccionarfotodegaleria(activity: Activity, code: Int){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /// Veridicar Seleccionar imagen
        when {
            requestCode == SELEC_ACTIVITY_IMAGE && resultCode == Activity.RESULT_OK ->{
                imageUri = data!!.data
                imageViewBook.setImageURI(imageUri)
            } }

        //Verificar reconocimiento
        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            edittextpalabraclave.setText(deSpeechToText)

        }

    }

    fun subirimagenylibro(id:String){
        progressDialog.show()
        val  storageReference = FirebaseStorage.getInstance().getReference("imagenes/imagen_$id")


        ////////////////
        storageReference.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val downloadUri = uriTask.result
            if (uriTask.isSuccessful){
                libroname = edittextnombrelibro.text.toString().capitalize()
                var librodescripcion =  edittextdescripcionlibro.text.toString().capitalize()
                var palabraclave = edittextpalabraclave.text.toString().capitalize()
                val dbReference = FirebaseDatabase.getInstance().getReference("libros")
                dbReference.child(id).setValue(hashMapOf(
                                "id" to "$id",
                                "name" to "$libroname",
                                "info" to "$librodescripcion",
                                "img" to "$downloadUri",
                                "pclave" to "$palabraclave"
                                ))
                        .addOnSuccessListener {taskSnapshot ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"EL libro fue creado con exito",Toast.LENGTH_LONG).show()
                            edittextnombrelibro.setText(null)
                            edittextdescripcionlibro.setText(null)
                            edittextpalabraclave.setText(null)
                            imageUri = null
                            imageViewBook.setImageDrawable(null)
                            edittextnombrelibro.requestFocus()
                        }
                        .addOnFailureListener {e ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

                        }

            }
        }

        //////////////////////
    }


    fun verificarBase(identificar:String){

        val ref = FirebaseDatabase.getInstance().getReference().child("libros")
        val buscar = ref.orderByChild("pclave").equalTo(identificar)
        buscar.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(this@CreateBook,"Esta palabra clave ya está asignada a un libro",Toast.LENGTH_LONG).show()
                }else{
                    var idlibro = System.currentTimeMillis().toString()
                    subirimagenylibro(idlibro)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CreateBook,error.message,Toast.LENGTH_SHORT).show()
            }
        })
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





}