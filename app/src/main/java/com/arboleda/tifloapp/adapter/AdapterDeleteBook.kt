package com.arboleda.tifloapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.databinding.RowDeletebookBinding
import com.arboleda.tifloapp.edits.EditBook
import com.arboleda.tifloapp.menulibros.FileListAdminActivity
import com.arboleda.tifloapp.menulibros.FilterBook
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_master_menu.view.*

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
        val img = model.img

        //establecer datos
        holder.librosTV.text = name

        val option = arrayOf( "Editar", "Eliminar")


        //manejar click eliminar libro
        holder.deleteBtn.setOnClickListener {
            // confirmar antes de eliminar
            val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar")
                    .setItems(option){dialog, position->

                        if (position==0){
                            val intent = Intent(context, EditBook::class.java)
                            intent.putExtra("bookid", id)
                            intent.putExtra("imageurl", img)
                            context.startActivity(intent)
                        }
                        else if (position ==1){
                            
                            Toast.makeText(context,"Eliminando.......", Toast.LENGTH_SHORT).show()
                            eliminarLibro(model,holder)
                        }

                    }.show()
            /**
                    .setMessage("Estas seguro de eliminar este libro?")
                    .setPositiveButton("Confirmar"){a, d->
                        Toast.makeText(context,"Eliminando.......", Toast.LENGTH_SHORT).show()
                        eliminarLibro(model,holder)
                    }
                    .setNegativeButton("Cancelar"){a, d->
                        a.dismiss()
                    }.show()*/
        }

        //handle click, start pdf list admin activity, also pas pdf id, title
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FileListAdminActivity::class.java)
            intent.putExtra("bookId", id)
            intent.putExtra("bookname", name)
            context.startActivity(intent)
        }

    }

    private fun eliminarLibro(model: ModelDeleteBook, holder: HolderDeleteBook) {
       // obtener id de la categoria a eliminar
        val id = model.id
        val url = model.img.toString()

        var storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        storageReference.delete()
            .addOnSuccessListener {

                // Firebase Realtime > Libros > IDlibro
                val ref = FirebaseDatabase.getInstance().getReference("libros")
                ref.child(id)
                    .removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(context,"Libro eliminado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context,"No se pudo eliminar el libro de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                    }

            }.addOnFailureListener {
                Toast.makeText(context,"No se pudo eliminar el libro de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
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