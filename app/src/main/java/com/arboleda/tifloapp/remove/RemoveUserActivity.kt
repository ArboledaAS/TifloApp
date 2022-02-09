package com.arboleda.tifloapp.remove

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arboleda.tifloapp.R
import com.arboleda.tifloapp.databinding.ActivityMainBinding
import com.arboleda.tifloapp.databinding.ActivityRemoveUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_remove_user.*

class RemoveUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemoveUserBinding

    private lateinit var userArrayList: ArrayList<ModelRemoveUser>
    private lateinit var  adapterRemoveUser: AdapterRemoveUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_remove_user)
        binding = ActivityRemoveUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUsers()

        binding.rbuttomregresar.setOnClickListener {
            finish()
        }


    }

    private fun loadUsers() {
        userArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userArrayList.clear()
                for (ds in snapshot.children){

                    val model = ds.getValue(ModelRemoveUser::class.java)

                    userArrayList.add(model!!)
                }
                adapterRemoveUser = AdapterRemoveUser(this@RemoveUserActivity, userArrayList)

                binding.recyvlerremoveuser.adapter = adapterRemoveUser
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}