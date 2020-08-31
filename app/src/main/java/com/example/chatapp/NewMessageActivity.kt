package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

    }
}

//class CustomAdapter: RecyclerView.Adapter<ViewHolder> {
//    override fun onBindViewHolder(holder: ViewHol der, position: Int) {
//        TODO("Not yet implemented")
//    }
//}