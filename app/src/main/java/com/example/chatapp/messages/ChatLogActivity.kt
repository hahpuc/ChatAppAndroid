package com.example.chatapp.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.R
import com.example.chatapp.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import android.util.Log
import com.example.chatapp.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "Chatlog"
    }

    var toUser: User? = null
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycleview_chat_log.adapter = adapter

        //val userName = intent.getStringExtra(NewMessageActivity.USER_KEY)
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser!!.userName

        //setUpDummyData()
        listenForMessages()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message!")
            performSendMessage()
        }
    }

    private fun listenForMessages() {

        val fromID = FirebaseAuth.getInstance().uid
        val toID = toUser?.uid
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID")

        reference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        val fromUser = LatestMessageActivity.currentUser?: return
                        adapter.add(ChatFromItem(chatMessage.text, fromUser))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    private fun performSendMessage() {
        // How do we actually send a message to firebase
        val text = edittext_chat_log.text.toString()

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        // toID: who be received message
        // fromID: who send message
        val toID = user!!.uid
        val fromID = FirebaseAuth.getInstance().uid

        if (fromID == null) return

//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toID/$fromID").push()
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromID/$toID")
        val toLatestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toID/$fromID")


        val chatMessage = ChatMessage(reference.key!!, text, fromID, toID, System.currentTimeMillis()/1000)

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Save our chat message: ${reference.key}")

            edittext_chat_log.text.clear()
            recycleview_chat_log.scrollToPosition(adapter.itemCount - 1)
        }

        toReference.setValue(chatMessage)

        latestMessageRef.setValue(chatMessage)
        toLatestMessageRef.setValue(chatMessage)
    }

//    private fun setUpDummyData() {
//        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(ChatFromItem("From NTL With Love ..."))
//        adapter.add(ChatToItem("TO NNYC with love ..."))
//
//
//        recycleview_chat_log.adapter = adapter
//    }
}

class ChatFromItem(val text: String, val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text

        // Load user profile image
        Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.imageView_from_row)
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String, val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text

        // Load user profile image
        Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.imageView_to_row)
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}