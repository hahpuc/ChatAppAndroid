package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add activity to the registerButton
        register_button_register.setOnClickListener {
            performRegister()
        }

        // Change to login screen
        already_have_account_text_view.setOnClickListener {
            Log.d("RegisterActivity", "Try to login");

            // Launch the activity login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        // Initialize objects
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        // Validate that email and password is not empty
        if (email.isEmpty() || password.isEmpty()) {
            // Make alert in screen
            Toast.makeText(this,"Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        // Access objects
        Log.d("RegisterActivity","Email is: " + email)
        Log.d("RegisterActivity","Password is: + $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // Else if successful
                Log.d("Register ", "Successfully create user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Register", "Fail to create user ${it.message}")
                Toast.makeText(this,"Fail to create user  ${it.message}", Toast.LENGTH_SHORT).show()

            }
    }
}