package com.arboleda.tifloapp.menus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.arboleda.tifloapp.AuthActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.menulibros.ContentAddActivity
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menulibros.DeleteBook
import com.arboleda.tifloapp.menulibros.PoesiaAddActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_master_menu.*
import kotlinx.android.synthetic.main.activity_simple_menu.*

class SimpleMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_menu)






        crearLibroCarview2.setOnClickListener {
            startActivity(Intent(this, CreateBook::class.java))
        }

        agregarPoesiaCarview2.setOnClickListener {
            startActivity(Intent(this, PoesiaAddActivity::class.java))
        }

        agregarArchivosCarview2.setOnClickListener {
            startActivity(Intent(this, ContentAddActivity::class.java))
        }





    }




    fun remove(){
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
            }.addOnFailureListener {

            }
        }

}