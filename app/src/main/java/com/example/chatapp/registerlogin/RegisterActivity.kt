package com.example.chatapp.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.models.User
import com.example.chatapp.messages.LatestMessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        // Add activity to selectPhoto button
        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity","Try to show photo selector")

            // Select photo in gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)

        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // Proceed and check what the selected image was ...
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageView_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f

            //val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }

    }

    private fun performRegister() {
        // Initialize objects
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        // Validate that email and password is not empty
        if (email.isEmpty() || password.isEmpty()) {
            // Make alert in screen
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }

        // Access objects
        Log.d("RegisterActivity", "Email is: " + email)
        Log.d("RegisterActivity", "Password is: + $password")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // Else if successful
                Log.d("RegisterActivity ", "Successfully create user with uid: ${it.result?.user?.uid}")

                // Upload image to firebase
                uploadImageToFirebase()
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Fail to create user ${it.message}")
                Toast.makeText(this, "Fail to create user  ${it.message}", Toast.LENGTH_SHORT)
                    .show()

            }
    }

    private fun uploadImageToFirebase() {

        if (selectedPhotoUri == null) return

        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File location: $it")
                    saveUsertoFirebase(it.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Fail to create user  ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun saveUsertoFirebase(profileImageURL: String) {
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            username_edittext_register.text.toString(),
            profileImageURL
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user  to Firebase")

                val intent = Intent(this, LatestMessageActivity::class.java)

                // Login to user's messages, so you can't back to the register/login screen.
                // When you open this app, you see the LastestMessageActivity
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}

