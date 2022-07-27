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
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.SecondUserListActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
                builder.setTitle("Seleccione una opción")
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
                        //Toast.makeText(context,"Libro eliminado", Toast.LENGTH_SHORT).show()
                        /**----------------------------------------------------------------------------------------------------------*/
                        val ref = FirebaseDatabase.getInstance().getReference("poesia")
                        ref.orderByChild("librosid").equalTo(id)
                                .addValueEventListener(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {

                                        /**  AQUI SE INICIA LA ELIMINACION DE TODOS LAS POESIAS RELACIONADA AL LIBRO
                                         * QUE SE QUIERE ELIMINAR  */
                                        if(snapshot.exists())
                                        {
                                            for (ds in snapshot.children){

                                                val model = ds.getValue(ModelUniversal::class.java)


                                                val ref2 = FirebaseDatabase.getInstance().getReference("poesia")
                                                ref2.child(model!!.id)
                                                        .removeValue()
                                                        .addOnSuccessListener {
                                                            //Toast.makeText(context,"Poesia ${model.name} eliminada", Toast.LENGTH_SHORT).show()

                                                            /**  AQUI SE INICIA LA ELIMINACION DE TODOS LOS ARCHIVOS RELACIONADOS A LAS
                                                             * POESIAS QUE SE QUIEREN ELIMINAR Y SU ARCHIVO RELACIONADO */

                                                            val ref3 = FirebaseDatabase.getInstance().getReference("Archivos").child(model.id)
                                                            ref3.orderByChild("poesiaid").equalTo(model.id)
                                                                    .addValueEventListener(object: ValueEventListener{
                                                                        override fun onDataChange(snapshot: DataSnapshot) {

                                                                            if(snapshot.exists()){
                                                                                for (ds in snapshot.children){

                                                                                    val model2 = ds.getValue(ModelUniversal::class.java)

                                                                                    var refstorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model2!!.url)
                                                                                    refstorageReference.delete()
                                                                                            .addOnSuccessListener {
                                                                                                val ref3 = FirebaseDatabase.getInstance().getReference("Archivos").child(model.id)
                                                                                                ref3.child(model2.id)
                                                                                                        .removeValue()
                                                                                                        .addOnSuccessListener {
                                                                                                            //Toast.makeText(context,"Archivo eliminado", Toast.LENGTH_SHORT).show()
                                                                                                        }
                                                                                                        .addOnFailureListener {
                                                                                                            Toast.makeText(context,"No se pudo eliminar el Archivo de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                                                                                                        }


                                                                                            }.addOnFailureListener {
                                                                                                //Toast.makeText(context,"No se pudo eliminar el Archvivo de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                                                                                            }


                                                                                }
                                                                            }

                                                                        }

                                                                        override fun onCancelled(error: DatabaseError) {

                                                                        }

                                                                    })


                                                        }
                                                        .addOnFailureListener {
                                                            Toast.makeText(context,"No se pudo eliminar la poesia de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                                                        }

                                            }

                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })


                        /**----------------------------------------------------------------------------------------------------------*/
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