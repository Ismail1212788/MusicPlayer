package com.example.myplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE = 101
    private  lateinit var username:String
    private  lateinit var pass:String
    private  lateinit var login:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login=findViewById(R.id.login)
        login.setOnClickListener {
            auth()
        }
    }

    fun auth(){
        username=findViewById<EditText>(R.id.username).text.toString().trim()
        pass= findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.passwordEditText).text.toString().trim()
        Log.e("chk","$username , $pass")

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        } else {
            if(username==Constant.username && pass==Constant.password)
            {
                Toast.makeText(this,"Authentication Successfull",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,Mediaplayer::class.java))
            }
            else{
                Toast.makeText(this,"Not valid user",Toast.LENGTH_LONG).show()
            }
        }
    }

    // Handle the permission request response
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
             auth()
            } else {
             Toast.makeText(this,"Permission Required",Toast.LENGTH_LONG).show()
            finish()
            }
        }
    }
}