package com.example.artourguideapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    val server: String = ""
    val model = Model()
    val view = View()
    val user = null
    val controller = Controller(server, model, view, user)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // update structures for app
        val updateStructuresIntent = Intent(this, UpdateStructures::class.java)
        startActivity(updateStructuresIntent)
    }
}