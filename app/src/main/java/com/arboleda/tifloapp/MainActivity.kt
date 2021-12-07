package com.arboleda.tifloapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.arboleda.tifloapp.menulibros.CreateBook

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
            R.id.menu_lector -> startActivity(Intent(this,CreateBook::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    

}