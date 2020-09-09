package com.example.chatapp.registerlogin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Add activity to loginButton
        login_button.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()

            Log.d("LoginActivity", "Login with email/pw: $email / $password ")

            // FirebaseAuth to signIn email/password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    Log.d("Login", "Successfully to login ")
                }
                .addOnFailureListener {
                    Log.d("Login",  "Failed to login")
                }
        }

        back_to_registration_text_view.setOnClickListener {
            finish()
        }
    }
}