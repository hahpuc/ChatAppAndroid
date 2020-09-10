package com.example.chatapp.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.messages.LatestMessageActivity
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

                    val intent = Intent(this, LatestMessageActivity::class.java)

                    // Login to user's messages, so you can't back to the register/login screen.
                    // When you open this app, you see the LastestMessageActivity
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
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