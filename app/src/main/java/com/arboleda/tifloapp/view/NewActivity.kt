package com.arboleda.tifloapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityNewBinding
import com.arboleda.tifloapp.uitel.getProgessDrawable
import com.arboleda.tifloapp.uitel.loadImage
import kotlinx.android.synthetic.main.activity_new.*

class NewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewBinding

    private var Url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_new)
        binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        val libroIntent = intent
        val libroname = libroIntent.getStringExtra("name")
        val libroinfo = libroIntent.getStringExtra("info")
        val libroimg = libroIntent.getStringExtra("img")

        name.text = libroname
        info.text = libroinfo
        img.loadImage(libroimg, getProgessDrawable(this))
        */
        val intent = intent
        Url = intent.getStringExtra("poesiaurl")!!
    }
}