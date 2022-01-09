package com.arboleda.tifloapp.menulibros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.adapter.AdapterDeleteBook
import com.arboleda.tifloapp.databinding.ActivityDeleteBookBinding
import com.arboleda.tifloapp.model.ModelDeleteBook
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeleteBook : AppCompatActivity() {

    private lateinit var categoryArrayList: ArrayList<ModelDeleteBook>
    private lateinit var adapterDeleteBook: AdapterDeleteBook
    private lateinit var  binding:ActivityDeleteBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBookBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_delete_book)
        setContentView(binding.root)
        loadCategories()

        binding.searchET.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterDeleteBook.filter.filter(s)
                }
                catch (e:Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()

        val  ref = FirebaseDatabase.getInstance().getReference("libros")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    val model =ds.getValue(ModelDeleteBook::class.java)

                    categoryArrayList.add(model!!)
                }
                adapterDeleteBook = AdapterDeleteBook(this@DeleteBook,categoryArrayList)

                binding.categoriesRv.adapter = adapterDeleteBook


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}