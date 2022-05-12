package com.arboleda.tifloapp.menulibros

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.adapter.AdapterSecondFileAdmin
import com.arboleda.tifloapp.databinding.ActivityMasterMenuBinding
import com.arboleda.tifloapp.databinding.ActivitySecondListAdminBinding
import com.arboleda.tifloapp.menus.MasterMenu
import com.arboleda.tifloapp.menus.ProviderType
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
        /**AQUI MODIFIQUEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE*/
        val ref = FirebaseDatabase.getInstance().getReference("Archivos").child(poesiaid)
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

                        adapterSecondFileAdmin = AdapterSecondFileAdmin(this@SecondListAdminActivity,secondListArrayList, poesiaid)
                        binding.booksRv.adapter = adapterSecondFileAdmin
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