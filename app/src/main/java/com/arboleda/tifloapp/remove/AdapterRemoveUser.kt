package com.arboleda.tifloapp.remove

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arboleda.tifloapp.databinding.RowRemoveUserAdminBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AdapterRemoveUser:RecyclerView.Adapter<AdapterRemoveUser.HolderRemoveUser> {

    private val context: Context
    private val userArrayList: ArrayList<ModelRemoveUser>

    private lateinit var binding: RowRemoveUserAdminBinding


    constructor(context: Context, userArrayList: ArrayList<ModelRemoveUser>) {
        this.context = context
        this.userArrayList = userArrayList
    }






    inner class  HolderRemoveUser(itemView: View):RecyclerView.ViewHolder(itemView){

        var emailTv:TextView = binding.emailTv
        var nivelTv:TextView = binding.nivelTv
        var removeBtn:ImageButton = binding.removeBtn

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRemoveUser {
        binding = RowRemoveUserAdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderRemoveUser(binding.root)
    }

    override fun onBindViewHolder(holder: HolderRemoveUser, position: Int) {

        val model = userArrayList[position]
        val email = model.email
        val id = model.id
        val nivel = model.nivel

        holder.emailTv.text = email
        if (nivel == "0"){
            holder.nivelTv.text = "Administrador"
        }
        else if (nivel == "1"){
            holder.nivelTv.text = "Creador de contenido"
        }

        holder.removeBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
                    .setMessage("Esta seguro de eliminar a este ususario")
                    .setPositiveButton("Confirmar"){a, d->
                        Toast.makeText(context, "Eliminando.....", Toast.LENGTH_SHORT).show()
                        deleteUser(model, holder)

                    }.setNegativeButton("Cancelar"){a, d->
                        a.dismiss()
                    }.show()
        }

    }

    private fun deleteUser(model: ModelRemoveUser, holder: HolderRemoveUser) {
        val id = model.id.trim()

        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(id)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context,"Usuario eliminado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context,"No se pudo eliminar el usuario de la base de datos: ${it.message}", Toast.LENGTH_SHORT).show()
                }

    }


    override fun getItemCount(): Int {
        return userArrayList.size
    }
}