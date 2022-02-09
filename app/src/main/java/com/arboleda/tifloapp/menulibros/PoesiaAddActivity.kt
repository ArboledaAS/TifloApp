package com.arboleda.tifloapp.menulibros

import android.app.*
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityPoesiaAddBinding
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.arboleda.tifloapp.model.ModelUniversal
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_poesia_add.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PoesiaAddActivity : AppCompatActivity() {
    //setup view binding
    private lateinit var binding: ActivityPoesiaAddBinding

    //firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //cuadro de diálogo progres (mostrar mientras se carga el archivo)
    private lateinit var progressDialog: ProgressDialog

    //Arraylist para contener categorías de archivos
    private lateinit var categoryArrayList: ArrayList<ModelUniversal>

    //uri de archivo elegido
    private var fileUri: Uri? = null
    private val RQ_ESCUCHA = 102


    //TAG
    private val TAG = "FILE_ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoesiaAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_poesia_add)

        firebaseAuth = FirebaseAuth.getInstance()
        loadFileBook()

        //diálogo de progreso de configuración
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espera Profavor")
        progressDialog.setCanceledOnTouchOutside(false)

        //
        binding.categoryTv.setOnClickListener {
            filePickDialog()
        }
/*
        binding.submitBtn2.setOnClickListener {
            filePickIntent()
        }*/

        binding.submitBtn.setOnClickListener {
            //paso 1: validar datos
            //paso 2: sube el archivo al almacenamiento de firebase
            //paso 3: obtenga la URL del archivo cargado
            //paso 4: sube la información del archivo a firebase db

            validateData()

        }

        binding.reconocimientoButton.setOnClickListener {
            entradaDeVoz()
        }

        binding.buttomregresar.setOnClickListener {
            finish()
        }


    }

    private var name = ""
    private var libro = ""
    private var pclave = ""

    private fun validateData() {
        //paso 1: validar datos
        Log.d(TAG,"validateData: Validando datos")

        //obtener datos
        name = binding.titleEt.text.toString().trim().capitalize()
        libro = binding.categoryTv.text.toString().trim()
        pclave = binding.descriptionEt.text.toString().trim().capitalize()

        //Validar datos
        if (name.isEmpty()){
            Toast.makeText(this,"Ingrese titulo...",Toast.LENGTH_SHORT).show()
        }
        else if (libro.isEmpty()){
            Toast.makeText(this,"Seleccione Libro...",Toast.LENGTH_SHORT).show()
        }
        else if (pclave.isEmpty()){
            Toast.makeText(this,"Porfavor ingrese una palabra clave para identificar la poesia",Toast.LENGTH_SHORT).show()
        }else if (pclave == "Libros" || pclave == "Libro" || pclave == "Lista" || pclave == "Listas"
                || pclave == "Ayuda" || pclave == "Comandos" || pclave == "Comando"
                || pclave == "Inicio" || pclave == "Atrás" || pclave == "Actualiazar"
                || pclave == "Inicio" || pclave == "Inicio" || pclave == "Inicio" ||
                pclave == "Reproducir"|| pclave == "Play" || pclave == "Reproduce" ||
                pclave == "Repetir" || pclave == "Reiniciar"){

            Toast.makeText(this,"Esta palabra esta reservada para el Sistema",Toast.LENGTH_LONG).show()
        }
        else{
            verificarBase(pclave)
        }

    }
/*
    private fun uploadFiletoStorage() {
        //paso 2: sube el archivo al almacenamiento de firebase
        Log.d(TAG,"uploadFiletoStorage: Subiendo al Almacenamiento...")

        //Mostrar dialogo del Progreso
        progressDialog.setMessage("Subiendo Archivo")
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val filePathAndName = "Archivos/$timestamp"

        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(fileUri!!)
                .addOnSuccessListener {taskSnapshot ->
                    Log.d(TAG,"uploadFiletoStorage: Subiendo Archivo")

                    //paso 3: obtenga la URL del archivo cargado
                    val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val uploadFileUrl = "${uriTask.result}"

                    uploadFileInfoToDb(uploadFileUrl, timestamp)
                }
                .addOnFailureListener{e ->
                    Log.d(TAG,"uploadFiletoStorage: Fallo la subida del archvio: ${e.message}")
                    progressDialog.dismiss()
                    Toast.makeText(this,"Fallo la subida del archvio: ${e.message}",Toast.LENGTH_SHORT).show()
                }
    }*/

    private fun uploadFileInfoToDb() {
        val timestamp = System.currentTimeMillis()

        //paso 4: sube la información del archivo a firebase db
        Log.d(TAG,"uploadFileInfoToDb: Subiendo a db")
        progressDialog.setMessage("Subiendo informacion del archivo....")

        //setup data upload
        val hashMap:HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["name"] = "$name"
        hashMap["librosid"] = "$selectBookId"
        hashMap["pclave"] = "$pclave"


        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.child("$timestamp")
                .setValue(hashMap)
                .addOnSuccessListener {
                    Log.d(TAG, "uploadFileInfoToDb: Subiendo a db")
                    progressDialog.dismiss()
                    Toast.makeText(this,"Poesia creada con exito ",Toast.LENGTH_LONG).show()
                    fileUri = null
                    binding.titleEt.setText(null)
                    binding.descriptionEt.setText(null)

                }
                .addOnFailureListener { e ->
                    Log.d(TAG,"uploadFileInfoToDb: Fallo la subida del archvio: ${e.message}")
                    progressDialog.dismiss()
                    Toast.makeText(this,"Fallo la subida del archvio: ${e.message}",Toast.LENGTH_SHORT).show()
                }

    }



    private fun loadFileBook() {
        Log.d(TAG, "Cargando Archivos del Libro")

        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Limpiar lista despues de agregar datos
                categoryArrayList.clear()
                for (ds in snapshot.children){

                    val model = ds.getValue(ModelUniversal::class.java)

                    categoryArrayList.add(model!!)
                    Log.d(TAG,"onDataChange: ${model.name}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private var selectBookId = ""
    private var selectBookTitle = ""

    private  fun filePickDialog(){
        Log.d(TAG, "filePickDialog: mostrando el cuadro de diálogo del selector de categorías de pdf ")

        val categoriesArray = arrayOfNulls<String>(categoryArrayList.size)
        for (i in categoryArrayList.indices){
            categoriesArray[i] = categoryArrayList[i].name
        }
        //alerta de dialogo
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elegir Libro")
                .setItems(categoriesArray){dialog, which ->
            //obtener un elemento en el que se hace click
            //manejar el elemento clic
            selectBookId  = categoryArrayList[which].id
            selectBookTitle = categoryArrayList[which].name



            //Establecer Libros en el textview
            binding.categoryTv.text = selectBookTitle

            Log.d(TAG,"filePickDialog: Seleccionar libro ID: $selectBookId")
            Log.d(TAG,"filePickDialog: Seleccionar libro ID: $selectBookTitle")


        }
        .show()
    }


    fun verificarBase(identificar:String){

        val ref = FirebaseDatabase.getInstance().getReference().child("poesia")
        val buscar = ref.orderByChild("pclave").equalTo(identificar)
        buscar.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(this@PoesiaAddActivity,"Esta palabra clave ya está asignada a una poesia",Toast.LENGTH_LONG).show()
                }else{
                    uploadFileInfoToDb()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PoesiaAddActivity,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            binding.descriptionEt.setText(deSpeechToText)


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






}