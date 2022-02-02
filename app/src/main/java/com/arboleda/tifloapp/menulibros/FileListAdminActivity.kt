package com.arboleda.tifloapp.menulibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.adapter.AdapterFileAdmin
import com.arboleda.tifloapp.databinding.ActivityFileListAdminBinding
import com.arboleda.tifloapp.model.ModelFile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.row_file_admin.*
import java.lang.Exception

class FileListAdminActivity : AppCompatActivity() {

    //
    private  lateinit var binding: ActivityFileListAdminBinding

    private companion object{
        const val TAG = "FILE_LIST_ADMIN_TAG"
    }

    //Libro ID, TITLE
    private  var bookId = ""
    private var bookname = ""

    private lateinit var fileArrayList: ArrayList<ModelFile>

    private lateinit var adapterFileAdmin: AdapterFileAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_file_list_admin)

        // obtener de la intención, que pasamos del adaptador
        val intent = intent
        bookId = intent.getStringExtra("bookId")!!
        bookname = intent.getStringExtra("bookname")!!


        binding.subTitleTv.text = bookname

        loadFileList()
/*
        //BUSCADOR
        binding.searchET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterFileAdmin.filter!!.filter(s)
                }
                catch (e: Exception){
                    Log.d(TAG,"onTextChanged: ${e.message}")
                }
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }
        })
*/
    }

    private fun loadFileList() {
        //
        fileArrayList = ArrayList()
        /*val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.orderByChild("librosid").equalTo(bookId)*/

        val ref = FirebaseDatabase.getInstance().getReference("poesia")
        ref.orderByChild("librosid").equalTo(bookId)
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        fileArrayList.clear()
                        for (ds in snapshot.children){

                            val model = ds.getValue(ModelFile::class.java)

                            if (model != null) {
                                fileArrayList.add(model)
                                Log.d(TAG,"onDataChange: ${model.name} ${model.id}")
                            }
                        }
                        adapterFileAdmin = AdapterFileAdmin(this@FileListAdminActivity, fileArrayList)
                        binding.booksRv.adapter = adapterFileAdmin
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }
}