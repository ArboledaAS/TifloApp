package com.arboleda.tifloapp.view.adapterview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.MyApplication
import com.arboleda.tifloapp.databinding.RowUser2ListBinding
import com.arboleda.tifloapp.databinding.RowUserListBinding
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.pdfs.PdfViewActivity
import com.arboleda.tifloapp.pdfs.TxtViewActivity
import com.arboleda.tifloapp.view.FinalActivity
import com.arboleda.tifloapp.view.SecondUserListActivity

class AdapterSecondUserList :RecyclerView.Adapter<AdapterSecondUserList.HolderSecondUserList>{

    private var context: Context

    private var archivosArrayList: ArrayList<ModelUniversal>



    private lateinit var binding: RowUser2ListBinding


    constructor(context: Context, archivosArrayList: ArrayList<ModelUniversal>) : super() {
        this.context = context
        this.archivosArrayList = archivosArrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSecondUserList {
        binding = RowUser2ListBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderSecondUserList(binding.root)
    }

    override fun onBindViewHolder(holder: HolderSecondUserList, position: Int) {
        val model = archivosArrayList[position]
        val id = model.id
        val pclave= model.pclave
        val poesiaid = model.poesiaid
        val url = model.url
        val tipo = model.tipo

        holder.titleTv.text = pclave

        MyApplication.loadCategory2(poesiaid,holder.categoryTv)

        holder.itemView.setOnClickListener {
            if (tipo == "0"){
                var intent = Intent(context, FinalActivity::class.java)
                intent.putExtra("poesiaurl", url)
                context.startActivity(intent)
            }
            else if (tipo == "1"){
                var intent = Intent(context, PdfViewActivity::class.java)
                intent.putExtra("pdfurl", url)
                context.startActivity(intent)
            }
            else if (tipo == "2"){
                var intent = Intent(context, TxtViewActivity::class.java)
                intent.putExtra("txturl", url)
                context.startActivity(intent)
            }

        }
    }

    override fun getItemCount(): Int {
        return archivosArrayList.size
    }

    inner class HolderSecondUserList(itemView: View) : RecyclerView.ViewHolder(itemView){

        val  titleTv = binding.titleTv
        val  categoryTv = binding.categoryTv

    }
}