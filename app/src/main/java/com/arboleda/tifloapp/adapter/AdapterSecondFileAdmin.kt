package com.arboleda.tifloapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.MyApplication
import com.arboleda.tifloapp.databinding.RowFile2AdminBinding
import com.arboleda.tifloapp.databinding.RowFileAdminBinding
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.arboleda.tifloapp.model.ModelFile
import com.arboleda.tifloapp.model.ModelUniversal
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdapterSecondFileAdmin  :RecyclerView.Adapter<AdapterSecondFileAdmin.HolderSecondFileAdmin>{


    private var context: Context

    private lateinit var id:String

    public var secondFileArrayList: ArrayList<ModelUniversal>





    private lateinit var binding: RowFile2AdminBinding


    ////constructor
    constructor(context: Context, secondFileArrayList: ArrayList<ModelUniversal>, id:String) : super() {
        this.context = context
        this.secondFileArrayList = secondFileArrayList
        this.id = id

    }

    inner class HolderSecondFileAdmin(itemView: View) : RecyclerView.ViewHolder(itemView){

        val  titleTv = binding.titleTv
        val  categoryTv = binding.categoryTv
        val  moreBtn = binding.moreBtn

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSecondFileAdmin {
        binding = RowFile2AdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderSecondFileAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderSecondFileAdmin, position: Int) {
        val model = secondFileArrayList[position]
        val archivoid = model.id
        val pclave = model.pclave
        val url = model.url
        val poesiaid = model.poesiaid

        holder.titleTv.text = pclave

        MyApplication.loadCategory2(poesiaid, holder.categoryTv)

        holder.moreBtn.setOnClickListener {
            moreOptionDialog(model, holder)
        }

    }

    private fun moreOptionDialog(model: ModelUniversal, holder: AdapterSecondFileAdmin.HolderSecondFileAdmin) {
        val bookId = model.id
        val bookUrl = model.url
        var bookTitle = model.name
        var prueba: AdapterFileAdmin

        val options = arrayOf("Eliminar")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Seleccionar opcion")
                .setItems(options){dialog,position ->
                    if (position == 0){
                        eliminarPoesia(model, holder)
                    }
                }
                .show()
    }


    private fun eliminarPoesia(model: ModelUniversal, holder: AdapterSecondFileAdmin.HolderSecondFileAdmin) {
        // obtener id de la categoria a eliminar
        val id = model.id
        val url = model.url
        var poesiaid = this.id

        /**AQUI */

        var storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        storageReference.delete()
            .addOnSuccessListener {
                val ref = FirebaseDatabase.getInstance().getReference("Archivos").child(poesiaid)
                ref.child(id)
                        .removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context,"Archivo eliminado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,"No se pudo eliminar el Archivo de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                        }


            }.addOnFailureListener {
                Toast.makeText(context,"No se pudo eliminar el Archvivo de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }





    override fun getItemCount(): Int {
        return secondFileArrayList.size
    }
}