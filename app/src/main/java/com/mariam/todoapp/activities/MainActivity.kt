package com.mariam.todoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mariam.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.goToSignupBtn.setOnClickListener {
            val intent = Intent(this, BasicActivity::class.java)
            intent.putExtra("FRAGMENT", "signup")
            startActivity(intent)
        }

        binding.goToLoginBtn.setOnClickListener {
            val intent = Intent(this, BasicActivity::class.java)
            intent.putExtra("FRAGMENT", "login")
            startActivity(intent)
        }
    }
}