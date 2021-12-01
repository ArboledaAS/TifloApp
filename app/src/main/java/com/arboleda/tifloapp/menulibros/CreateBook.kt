package com.arboleda.tifloapp.menulibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arboleda.tifloapp.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_book.*

class CreateBook : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_book)



        guardarlibroButton.setOnClickListener {
            if(edittextnombrelibro.text.toString().isNotEmpty()){
            db.collection("libros").document(edittextnombrelibro.text.toString())
                .set(hashMapOf(
                    "audio" to "",
                    "video" to "",
                    "todo" to ""
                ))}else{
                Toast.makeText(this,"Ingrese texto en el campo", Toast.LENGTH_SHORT).show()}
        }
    }
}