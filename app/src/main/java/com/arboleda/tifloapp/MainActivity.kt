package com.arboleda.tifloapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.arboleda.tifloapp.adapter.AdapterDeleteBook
import com.arboleda.tifloapp.adapter.LibrosAdapter
import com.arboleda.tifloapp.menulibros.CreateBook
import com.arboleda.tifloapp.menulibros.DeleteBook
import com.arboleda.tifloapp.menus.MasterMenu
import com.arboleda.tifloapp.model.LibroData
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mDatabase:DatabaseReference
    private lateinit var libroList:ArrayList<LibroData>
    private lateinit var mAdapter:LibrosAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //INICIADOR
        libroList = ArrayList()
        mAdapter = LibrosAdapter(this,libroList)
        recyclerLibros.layoutManager = LinearLayoutManager(this)
        recyclerLibros.setHasFixedSize(true)
        recyclerLibros.adapter = mAdapter

        getLibrosData()


    }



    private fun getLibrosData() {
        mDatabase =FirebaseDatabase.getInstance().getReference("libros")
        mDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (libroSnapshot in snapshot.children){
                        val libro = libroSnapshot.getValue(LibroData::class.java)
                        libroList.add(libro!!)
                    }
                    recyclerLibros.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    ////////////////inicializa el menu escritor y lector
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //R.id.menu_escritor -> startActivity(Intent(this,CreateBook::class.java))
            R.id.menu_escritor -> startActivity(Intent(this,LoginActivity::class.java))
            R.id.menu_lector -> startActivity(Intent(this,MasterMenu::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    

}