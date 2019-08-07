package com.glureau.poc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.glureau.poc.register.RegisterFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, RegisterFragment.newInstance())
            .commit()
    }
}
