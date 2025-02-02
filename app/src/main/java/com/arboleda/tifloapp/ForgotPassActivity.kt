package com.arboleda.tifloapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pass.*

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        txtEmail=findViewById(R.id.txtEmail)
        auth=FirebaseAuth.getInstance()
        progressBar=findViewById(R.id.progressBar)


        buttomregresar.setOnClickListener {
            finish()
        }
    }

    fun send(view: View){
        val email=txtEmail.text.toString()

        if (!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this){
                        task->if (task.isSuccessful){
                    progressBar.visibility= View.VISIBLE
                    finish()
                }else{
                    Toast.makeText(this,"Error al enviar el email", Toast.LENGTH_SHORT).show()
                }
                }
        }
    }
}