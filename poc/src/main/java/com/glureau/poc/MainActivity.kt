package com.glureau.poc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glureau.poc.register.RegisterFragment
import com.glureau.poc.users.UsersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_register, RegisterFragment.newInstance())
            .replace(R.id.container_users, UsersFragment.newInstance())
            .commit()
    }
}
