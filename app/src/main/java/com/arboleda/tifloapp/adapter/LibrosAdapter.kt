package com.arboleda.tifloapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ItemListBinding
import com.arboleda.tifloapp.model.LibroData
import com.arboleda.tifloapp.view.FirstUserListActivity
import com.arboleda.tifloapp.view.NewActivity

class LibrosAdapter(
    var c:Context,var  libroList:ArrayList<LibroData>
):RecyclerView.Adapter<LibrosAdapter.LibroViewHolder>()
{
    inner class LibroViewHolder(var v:ItemListBinding):RecyclerView.ViewHolder(v.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemListBinding>(
            inflater,R.layout.item_list,parent,false)
        return LibroViewHolder(v)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val newList = libroList[position]
        holder.v.isLibros = libroList[position]
        holder.v.root.setOnClickListener {
            //val img = newList.img
            val name = newList.name
            val info = newList.info
            val id = newList.id

            val mIntent = Intent(c,FirstUserListActivity::class.java)
            mIntent.putExtra("bookId", id)
            //mIntent.putExtra("img", img)
            mIntent.putExtra("bookname", name)
            mIntent.putExtra("bookinfo", info)
            //mIntent.putExtra("info", info)
            c.startActivity(mIntent)
        }
    }

    override fun getItemCount(): Int {
        return libroList.size
    }
    /*esta clase generada automáticamente */
}