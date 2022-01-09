package com.arboleda.tifloapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.databinding.RowDeletebookBinding
import com.arboleda.tifloapp.menulibros.FilterBook
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.google.firebase.database.FirebaseDatabase

class AdapterDeleteBook :RecyclerView.Adapter<AdapterDeleteBook.HolderDeleteBook>, Filterable{

    private  val context:Context
    public var  categoryArrayList:ArrayList<ModelDeleteBook>
    private var  filterList: ArrayList<ModelDeleteBook>

    private var filter: FilterBook? = null

    private  lateinit var binding: RowDeletebookBinding

    //contructor
    constructor(context: Context, categoryArrayList: ArrayList<ModelDeleteBook>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    ///////
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDeleteBook {
        // VINCULAR INFLATE row_deletebook.xml
        binding = RowDeletebookBinding.inflate(LayoutInflater.from(context),parent,false)
        return  HolderDeleteBook(binding.root)
    }

    override fun onBindViewHolder(holder: HolderDeleteBook, position: Int) {
        // Obtener datos , establecer datos, manejar click
        val model = categoryArrayList[position]
        val id = model.id
        val name = model.name

        //establecer datos
        holder.librosTV.text = name

        //manejar click eliminar libro
        holder.deleteBtn.setOnClickListener {
            // confirmar antes de eliminar
            val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar")
                    .setMessage("Estas seguro de eliminar este libro?")
                    .setPositiveButton("Confirmar"){a, d->
                        Toast.makeText(context,"Eliminando.......", Toast.LENGTH_SHORT).show()
                        eliminarLibro(model,holder)
                    }
                    .setNegativeButton("Cancelar"){a, d->
                        a.dismiss()
                    }.show()
        }

    }

    private fun eliminarLibro(model: ModelDeleteBook, holder: HolderDeleteBook) {
       // obtener id de la categoria a eliminar
        val id = model.id
        // Firebase Realtime > Libros > IDlibro
        val ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Libro eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context,"No se pudo eliminar el libro de la base de datos", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size // numero de items en la lista
    }



    //ViewHolder para mantener / Vista de inicio para row_deletbook.xml
    inner class HolderDeleteBook(itemView: View):RecyclerView.ViewHolder(itemView){
        //vista de la interfaz de usuario de init
        var librosTV:TextView = binding.librosTv
        var deleteBtn:ImageButton = binding.deleteBtn
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterBook(filterList, this)
        }
        return filter as FilterBook
    }


}