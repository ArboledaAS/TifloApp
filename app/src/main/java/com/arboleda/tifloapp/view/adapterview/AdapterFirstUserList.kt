package com.arboleda.tifloapp.view.adapterview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.MyApplication
import com.arboleda.tifloapp.databinding.RowUserListBinding
import com.arboleda.tifloapp.menulibros.SecondListAdminActivity
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.SecondUserListActivity

class AdapterFirstUserList :RecyclerView.Adapter<AdapterFirstUserList.HolderFirstUserList>{

    //context
    private var context: Context

    //arrarlist para sostener poesia
    private var poesiaArrayList: ArrayList<ModelUniversal>

    //viewBinding
    private lateinit var binding: RowUserListBinding

    //constructor
    constructor(context: Context, poesiaArrayList: ArrayList<ModelUniversal>) : super() {
        this.context = context
        this.poesiaArrayList = poesiaArrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFirstUserList {
        binding = RowUserListBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderFirstUserList(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFirstUserList, position: Int) {

        //Obtener datos
        val model = poesiaArrayList[position]
        val poesiaid = model.id
        val bookid = model.librosid
        val name = model.name
        val pclave = model.pclave

        //establecer datos
        holder.titleTv.text = name
        holder.palabraclaveTv.text = pclave

        //cargar más detalles como libro, poesía

        //libro id
        MyApplication.loadCategory1(bookid,holder.categoryTv)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SecondUserListActivity::class.java)
            intent.putExtra("poesiaid", poesiaid)
            intent.putExtra("bookid", bookid)
            intent.putExtra("poesianame", name)
            intent.putExtra("pclavepoesia", pclave)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return poesiaArrayList.size //recuento de elementos
    }

    //view holder class para row_user_list.xml
    inner class HolderFirstUserList(itemView: View): RecyclerView.ViewHolder(itemView){
        //UI view del row_user_list.xml
        val  titleTv = binding.titleTv
        val  palabraclaveTv = binding.palabraclaveTv
        val  categoryTv = binding.categoryTv

    }

}