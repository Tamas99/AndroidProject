package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


/**
 * In our MainActivity we just set the view for our layout,
 * it has nothing other functionality
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}