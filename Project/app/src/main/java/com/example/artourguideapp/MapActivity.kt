package com.example.artourguideapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

/**
 * This activity is simply a web view that displays WWU's interactive campus map.
 * The url for this map is https://www.wwu.edu/map/
 */
class MapActivity : AppCompatActivity() {

    /** The WebView to display the campus map */
    private lateinit var map: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        map = findViewById(R.id.campus_map)
        if (savedInstanceState == null) {
            map.loadUrl("https://www.wwu.edu/map/")
            map.settings.javaScriptEnabled = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        map.restoreState(savedInstanceState)
    }
}