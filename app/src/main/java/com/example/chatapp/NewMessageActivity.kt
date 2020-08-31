package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        // Fetch User
        fetchUser()
    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                // User data loop
                snapshot.children.forEach() {
                    Log.d("NewMessage", it.toString())

                    val user = it.getValue(User::class.java)
                    if (user != null)
                        adapter.add(UserItem(user))
                }

                recycleview_newmessage.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        // Get user's name to each viewHolder
        viewHolder.itemView.username_textview_newmessage.text = user.userName

        // Get user's avatar
        Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.userphoto_newmessage)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}

//class CustomAdapter: RecyclerView.Adapter<ViewHolder> {
//    override fun onBindViewHolder(holder: ViewHol der, position: Int) {
//        TODO("Not yet implemented")
//    }
//}