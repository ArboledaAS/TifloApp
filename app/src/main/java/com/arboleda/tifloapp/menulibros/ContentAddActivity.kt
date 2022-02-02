
package com.arboleda.tifloapp.menulibros

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }



    private var pclave = ""
    private var poesia = ""
    private fun validateData() {
        //paso 1: validar datos
        Log.d(TAG, "validateData: Validando datos")

        //obtener datos
        pclave = binding.titleEt1.text.toString().trim()
        poesia = binding.categoryTv1.text.toString().trim()

        //Validar datos
        if (pclave.isEmpty()) {
            Toast.makeText(this, "Seleccione Poesia para asociar...", Toast.LENGTH_SHORT).show()
        }
        else if (poesia.isEmpty()) {
            Toast.makeText(
                this, "Porfavor ingrese una palabra clave para identificar el archivo de la poesia",Toast.LENGTH_SHORT).show()
        }else if(fileUri == null){
            Toast.makeText(this,"Porfavor elija un archivo...",Toast.LENGTH_SHORT).show()
        }
        else{
            uploadFiletoStorage()
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
                Toast.makeText(this,"El archivo se subio exitosamente",Toast.LENGTH_SHORT).show()
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


}