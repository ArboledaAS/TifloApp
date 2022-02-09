package com.arboleda.tifloapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.MyApplication
import com.arboleda.tifloapp.databinding.RowFileAdminBinding
import com.arboleda.tifloapp.menulibros.FilterFileAdmin
import com.arboleda.tifloapp.model.ModelFile
import com.arboleda.tifloapp.menulibros.SecondListAdminActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class AdapterFileAdmin :RecyclerView.Adapter<AdapterFileAdmin.HolderFileAdmin>, Filterable{

    //context
    private var  context: Context

    //array list que sostiene archivos
    public var fileArrayList: ArrayList<ModelFile>
    private val filterList: ArrayList<ModelFile>



    //ViewBinding
    private  lateinit var binding:RowFileAdminBinding

    //
    private var filter: FilterFileAdmin? = null

    //Constructor
    constructor(context: Context, fileArrayList: ArrayList<ModelFile>) : super() {
        this.context = context
        this.fileArrayList = fileArrayList
        this.filterList = fileArrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFileAdmin {
        //bind / inflate layout
        binding = RowFileAdminBinding.inflate(LayoutInflater.from(context),parent,false)

        return HolderFileAdmin(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFileAdmin, position: Int) {
        // Obtener datos , establecer datos, manejar click

        //obtener datos
        val model = fileArrayList[position]
        val fileId = model.id
        val librosid = model.librosid
        val title = model.name
        val descrption = model.descripcion
        val fileUrl = model.url

        //set data
        holder.titleTv.text = title
        holder.descriptionTv.text = descrption

        //ID DEL LIBRO
        MyApplication.loadCategory1(librosid, holder.categoryTv)

        holder.moreBtn.setOnClickListener {
            moreOptionDialog(model, holder)


        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SecondListAdminActivity::class.java)
            intent.putExtra("poesiaid", fileId)
            intent.putExtra("poesianame", title)
            context.startActivity(intent)
        }

    }

    private fun moreOptionDialog(model: ModelFile, holder: AdapterFileAdmin.HolderFileAdmin) {
        val bookId = model.id
        val bookUrl = model.url
        var bookTitle = model.name

        val options = arrayOf("Eliminar")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Seleccionar opcion")
                .setItems(options){dialog,position ->
                    if (position == 0){
                        //MyApplication.deleteFile(context,bookId, bookTitle,bookUrl )
                        eliminarPoesia(model, holder)
                    }
                }
                .show()
    }

    fun eliminarPoesia(model: ModelFile, holder: AdapterFileAdmin.HolderFileAdmin){
        var id = model.id

        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Poesia eliminada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context,"No se pudo eliminar la poesia de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun getItemCount(): Int {
        return fileArrayList.size //recuento de elementos
    }



    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterFileAdmin(filterList, this)
        }

        return filter as FilterFileAdmin
    }

    //view holder class para row_file_admin
    inner class  HolderFileAdmin(itemView: View): RecyclerView.ViewHolder(itemView){

        //UI vista row file admin
        //val  progressBar = binding.progressBar
        val  titleTv = binding.titleTv
        val  descriptionTv = binding.descriptionTv
        val  categoryTv = binding.categoryTv
        val  moreBtn = binding.moreBtn

    }

}