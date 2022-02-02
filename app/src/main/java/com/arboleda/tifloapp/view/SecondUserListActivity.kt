package com.arboleda.tifloapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivitySecondUserListBinding
import com.arboleda.tifloapp.model.ModelUniversal
import com.arboleda.tifloapp.view.adapterview.AdapterFirstUserList
import com.arboleda.tifloapp.view.adapterview.AdapterSecondUserList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_second_user_list.*

class SecondUserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondUserListBinding

    private companion object{
        const val TAG = "FILE_LIST_ADMIN_TAG"
    }

    private lateinit var fileArrayList: ArrayList<ModelUniversal>

    private lateinit var adapterFileAdmin: AdapterSecondUserList



    private  var poesiaid = ""
    private  var bookId = ""
    private var poesianame = ""
    private var pclavepoesia = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_second_user_list)
        binding = ActivitySecondUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        poesiaid = intent.getStringExtra("poesiaid")!!
        bookId = intent.getStringExtra("bookid")!!
        poesianame = intent.getStringExtra("poesianame")!!
        pclavepoesia = intent.getStringExtra("pclavepoesia")!!


        loadFileList()


    }


    

    private fun loadFileList() {
        fileArrayList = ArrayList()
        /*val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.orderByChild("librosid").equalTo(bookId)*/

        val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.orderByChild("poesiaid").equalTo(poesiaid)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        fileArrayList.clear()
                        for (ds in snapshot.children){

                            val model = ds.getValue(ModelUniversal::class.java)

                            if (model != null) {
                                fileArrayList.add(model)
                                Log.d(TAG,"onDataChange: ${model.name} ${model.id}")
                            }
                        }
                        adapterFileAdmin = AdapterSecondUserList(this@SecondUserListActivity, fileArrayList)
                        binding.recycleruser2.adapter = adapterFileAdmin
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }

}