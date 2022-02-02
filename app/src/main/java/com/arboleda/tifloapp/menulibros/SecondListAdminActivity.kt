package com.arboleda.tifloapp.menulibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.arboleda.tifloapp.adapter.AdapterSecondFileAdmin
import com.arboleda.tifloapp.databinding.ActivitySecondListAdminBinding
import com.arboleda.tifloapp.model.ModelUniversal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_second_list_admin.*

class SecondListAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondListAdminBinding

    private companion object{
        const val TAG = "FILE_LIST_ADMIN_TAG"
    }

    private var poesiaid = ""
    private var poesianame = ""

    private lateinit var secondListArrayList: ArrayList<ModelUniversal>

    private lateinit var adapterSecondFileAdmin: AdapterSecondFileAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondListAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_second_list_admin)

        val intent = intent
        poesiaid = intent.getStringExtra("poesiaid")!!
        poesianame = intent.getStringExtra("poesianame")!!

        binding.subTitleTv.text = poesianame

        loadSecondFileList()
    }

    private fun loadSecondFileList() {
        secondListArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Archivos")
        ref.orderByChild("poesiaid").equalTo(poesiaid)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        secondListArrayList.clear()
                        for (ds in snapshot.children){

                            val model = ds.getValue(ModelUniversal::class.java)

                            if (model != null) {
                                secondListArrayList.add(model)
                                Log.d(TAG,"onDataChange: ${model.pclave} ${model.id}")
                            }
                        }

                        adapterSecondFileAdmin = AdapterSecondFileAdmin(this@SecondListAdminActivity,secondListArrayList)
                        binding.booksRv.adapter = adapterSecondFileAdmin
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }
}