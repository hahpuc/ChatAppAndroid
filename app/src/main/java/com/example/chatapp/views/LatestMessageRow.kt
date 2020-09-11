package com.example.chatapp.views

import com.example.chatapp.R
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<ViewHolder>() {

    var chatPartnerUser: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_latest_message.text = chatMessage.text

        val chatPartnerID: String
        if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
            chatPartnerID = chatMessage.toID
        } else {
            chatPartnerID = chatMessage.fromID
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.username_latest_message.text = chatPartnerUser?.userName

                Picasso.get().load(chatPartnerUser?.profileImageURL).into(viewHolder.itemView.imageView_latest_message)

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}