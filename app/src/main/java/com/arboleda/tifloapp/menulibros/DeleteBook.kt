package com.arboleda.tifloapp.menulibros

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.arboleda.tifloapp.LoginActivity
import com.arboleda.tifloapp.MainActivity
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.adapter.AdapterDeleteBook
import com.arboleda.tifloapp.databinding.ActivityDeleteBookBinding
import com.arboleda.tifloapp.menus.MasterMenu
import com.arboleda.tifloapp.menus.ProviderType
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


    ////////////////inicializa el menu escritor y lector

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu2, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //R.id.menu_escritor -> startActivity(Intent(this,CreateBook::class.java))
            R.id.menu_casa -> {sesion()}

        }
        return super.onOptionsItemSelected(item)
    }
    ////////////////inicializa el menu escritor y lector*******


    fun sesion(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var email = prefs.getString("email", null)
        var emailname = prefs.getString("emailname", null)
        var provider = prefs.getString("provider", null)

        if (email != null && provider != null){
            showHome(email, emailname.toString(), ProviderType.BASIC)
        }

    }

    private fun showHome(email: String, emailname:String, provider: ProviderType){
        val homeIntent = Intent (this, MasterMenu::class.java ).apply {

            putExtra("email", email)
            putExtra("emailname", emailname)
            putExtra("provider", provider.name)
        }
        finish()
        startActivity(homeIntent)
    }


}