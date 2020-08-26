package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add activity to the registerButton
        register_button_register.setOnClickListener {
            // Initialize objects
            val email = email_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()

            // Access objects
            Log.d("MainActivity","Email is: " + email)
            Log.d("MainActivity","Password is: + $password")
        }

        // Change to login screen
        already_have_account_text_view.setOnClickListener {
            Log.d("MainActivity", "Try to login");

            // Launch the activity login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}