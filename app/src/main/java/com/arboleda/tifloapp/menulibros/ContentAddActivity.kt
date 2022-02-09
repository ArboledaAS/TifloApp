
package com.arboleda.tifloapp.menulibros

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.arboleda.tifloapp.databinding.ActivityContentAddBinding
import com.arboleda.tifloapp.model.ModelUniversal
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ContentAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentAddBinding

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
        binding = ActivityContentAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_content_add)
        firebaseAuth = FirebaseAuth.getInstance()
        loadFileBook()

        //diálogo de progreso de configuración
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espera Profavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.categoryTv1.setOnClickListener {
            filePickDialog()
        }

        binding.submitBtn1.setOnClickListener {
            //paso 1: validar datos
            //paso 2: sube el archivo al almacenamiento de firebase
            //paso 3: obtenga la URL del archivo cargado
            //paso 4: sube la información del archivo a firebase db

            validateData()

        }

        binding.submitBtn2.setOnClickListener {
            filePickIntent()
        }

        binding.reconocimientoButton.setOnClickListener {
            entradaDeVoz()
        }

        binding.buttomregresar.setOnClickListener {
            finish()
        }

    }



    private var pclave = ""
    private var poesia = ""
    private fun validateData() {
        //paso 1: validar datos
        Log.d(TAG, "validateData: Validando datos")

        //obtener datos
        pclave = binding.titleEt1.text.toString().trim().capitalize()
        poesia = binding.categoryTv1.text.toString().trim()

        //Validar datos
        if (pclave.isEmpty()) {
            Toast.makeText(this, "Porfavor ingrese una palabra clave para identificar " +
                    "el archivo de la poesia ", Toast.LENGTH_LONG).show()
        }
        else if (poesia.isEmpty()) {
            Toast.makeText(
                this, "Seleccione Poesia para asociar...",Toast.LENGTH_LONG).show()
        }else if(fileUri == null){
            Toast.makeText(this,"Porfavor elija un archivo...",Toast.LENGTH_LONG).show()
        }else if (pclave == "Libros" || pclave == "Libro" || pclave == "Lista" || pclave == "Listas"
                || pclave == "Ayuda" || pclave == "Comandos" || pclave == "Comando"
                || pclave == "Inicio" || pclave == "Atrás" || pclave == "Actualiazar"
                || pclave == "Inicio" || pclave == "Inicio" || pclave == "Inicio" ||
                pclave == "Reproducir"|| pclave == "Play" || pclave == "Reproduce" ||
                pclave == "Repetir" || pclave == "Reiniciar"){

        }
        else{
            verificarBase(pclave)

        }
    }

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
    }

    private fun uploadFileInfoToDb(uploadFileUrl: String, timestamp:Long) {
        //paso 4: sube la información del archivo a firebase db
        Log.d(TAG,"uploadFileInfoToDb: Subiendo a db")
        progressDialog.setMessage("Subiendo informacion del archivo....")

        //setup data upload
        val hashMap:HashMap<String, Any> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["pclave"] = "$pclave"
        hashMap["poesiaid"] = "$selectBookId"
        hashMap["url"] = "$uploadFileUrl"

        val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadFileInfoToDb: Subiendo a db")
                progressDialog.dismiss()
                Toast.makeText(this,"El archivo se subio exitosamente",Toast.LENGTH_LONG).show()
                binding.titleEt1.setText(null)
                fileUri = null

            }
            .addOnFailureListener { e ->
                Log.d(TAG,"uploadFileInfoToDb: Fallo la subida del archvio: ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Fallo la subida del archvio: ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadFileBook() {
        Log.d(TAG, "Cargando Archivos de Poesias")

        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
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
    private fun filePickDialog() {

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

                //iiddbook = selectBookId

                //Establecer Libros en el textview
                binding.categoryTv1.text = selectBookTitle

                Log.d(TAG,"filePickDialog: Seleccionar libro ID: $selectBookId")
                Log.d(TAG,"filePickDialog: Seleccionar libro Nombre: $selectBookTitle")


            }
            .show()
    }

    private fun filePickIntent() {
        Log.d(TAG, "filePickIntent: Stark file pick intent")

        val intent = Intent()
        intent.type = "file/*"
        intent.action = Intent.ACTION_GET_CONTENT
        fileActivityResultlauncher.launch(intent)
    }

    val fileActivityResultlauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult>{result ->
                if (result.resultCode == RESULT_OK){
                    Log.d(TAG, "Archivo seleccionado")
                    fileUri = result.data!!.data
                }else{
                    Log.d(TAG,"Seleccion de archivo cancelado")
                    Toast.makeText(this,"Cancelado", Toast.LENGTH_SHORT).show()
                }
            }
    )




    fun verificarBase(identificar:String){

        val ref = FirebaseDatabase.getInstance().getReference().child("Archivos")
        val buscar = ref.orderByChild("pclave").equalTo(identificar)
        buscar.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(this@ContentAddActivity,"Esta palabra clave ya está asignada a un Archivo",Toast.LENGTH_LONG).show()
                }else{
                    uploadFiletoStorage()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ContentAddActivity,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val deSpeechToText = result?.get(0).toString().capitalize()
            binding.titleEt1.setText(deSpeechToText)


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