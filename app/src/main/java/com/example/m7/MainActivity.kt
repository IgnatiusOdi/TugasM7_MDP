package com.example.m7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.m7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private lateinit var users: MutableList<UserEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = AppDatabase.build(this)
        users = mutableListOf()

        coroutine.launch {
            refresh()
        }


    }
}