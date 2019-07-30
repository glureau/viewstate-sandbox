package com.glureau.viewstatepattern

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glureau.viewstatepattern.view_state.ViewStateFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, ViewStateFragment.newInstance())
            .commit()
    }
}
