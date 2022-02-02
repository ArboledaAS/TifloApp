package com.arboleda.tifloapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.MyApplication
import com.arboleda.tifloapp.databinding.RowFile2AdminBinding
import com.arboleda.tifloapp.databinding.RowFileAdminBinding
import com.arboleda.tifloapp.model.ModelFile
import com.arboleda.tifloapp.model.ModelUniversal

class AdapterSecondFileAdmin  :RecyclerView.Adapter<AdapterSecondFileAdmin.HolderSecondFileAdmin>{

    private var context: Context

    public var secondFileArrayList: ArrayList<ModelUniversal>





    private lateinit var binding: RowFile2AdminBinding


    ////constructor
    constructor(context: Context, secondFileArrayList: ArrayList<ModelUniversal>) : super() {
        this.context = context
        this.secondFileArrayList = secondFileArrayList

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

    }

    private fun moreOptionDialog(model: ModelUniversal, holder: AdapterSecondFileAdmin.HolderSecondFileAdmin) {
        val bookId = model.id
        val bookUrl = model.url
        var bookTitle = model.name

        val options = arrayOf("Eliminar")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Seleccionar opcion")
                .setItems(options){dialog,position ->
                    if (position == 0){

                    }
                }
                .show()
    }

    override fun getItemCount(): Int {
        return secondFileArrayList.size
    }
}