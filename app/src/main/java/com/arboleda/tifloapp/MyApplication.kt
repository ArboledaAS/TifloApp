package com.arboleda.tifloapp

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{
        fun loadCategory1(categoryId: String, categoryTv: TextView){
            //usando libros id de firebase
            val ref = FirebaseDatabase.getInstance().getReference("libros")
            ref.child(categoryId)
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            //get libro
                            val nombre = "${snapshot.child("name").value}"

                            //set libro
                            categoryTv.text = nombre
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

        }
        //////////////////////////////////////////////////////////////
        fun loadCategory2(categoryId: String, categoryTv: TextView){

            val ref = FirebaseDatabase.getInstance().getReference("poesia")
            ref.child(categoryId)
                    .addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            //get clibro
                            val nombre = "${snapshot.child("name").value}"

                            //set libro
                            categoryTv.text = nombre
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

        }
        //////////////////////////////////////////////////////////////

        fun deleteFile(context: Context, fileId: String, fileTitle:String, fileUrl: String){
            val TAG = "ELIMINAR_ARCHIVO_TAG"
            Log.d(TAG, "deleteFile: Eliminando...")

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Espere Porfavor")
            progressDialog.setMessage("Eliminando $fileTitle....")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            Log.d(TAG, "deleteFile: Eliminando de la base de datos...")
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl)
            storageReference.delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "deleteFile: Eliminado de la Base...")

                        val ref = FirebaseDatabase.getInstance().getReference("Archivos")
                        ref.child(fileId)
                                .removeValue()
                                .addOnSuccessListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(context, "Eliminancion satisfactoria...",Toast.LENGTH_SHORT).show()
                                    Log.d(TAG, "deleteFile: Eliminado de la Base de datos...")
                                }
                                .addOnFailureListener { e->
                                    progressDialog.dismiss()
                                    Log.d(TAG, "deleteFile: Fallo la eliminacion del archivo ${e.message}")
                                    Toast.makeText(context,"Fallo la eliminacion del archivo ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                    }
                    .addOnFailureListener { e->
                        progressDialog.dismiss()
                        Log.d(TAG, "deleteFile: Fallo la eliminacion del archivo ${e.message}")
                        Toast.makeText(context,"Fallo la eliminacion del archivo ${e.message}", Toast.LENGTH_SHORT).show()
                    }
        }



    }


}