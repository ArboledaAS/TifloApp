package com.arboleda.tifloapp.menus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arboleda.tifloapp.AuthActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.menulibros.CreateBook
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_master_menu.*

class MasterMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_menu)

        /*
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion exitosa con la base de datos")
        analytics.logEvent("InitScreen", bundle)*/
        button1.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        button2.setOnClickListener {
            startActivity(Intent(this, CreateBook::class.java))
        }
        button3.setOnClickListener {

        }
    }
}