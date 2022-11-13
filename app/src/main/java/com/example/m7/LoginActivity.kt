package com.example.m7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.m7.AppDatabase.Companion.db
import com.example.m7.UserEntity.Companion.listUser
import com.example.m7.UserEntity.Companion.userLoggedIn
import com.example.m7.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppDatabase.build(this)

        coroutine.launch {
            refreshListUser()
        }

        binding.btLoginLogin.setOnClickListener {
            if (loginCheck()) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            loginCheck()
        }

        binding.btToRegisterLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private suspend fun refreshListUser() {
        listUser.clear()
        listUser.addAll(db.userDao.fetch().toMutableList())
    }

    private fun loginCheck(): Boolean {
        val username = binding.etUsernameLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        if (username.isBlank() || password.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this, "Field kosong!", Toast.LENGTH_SHORT).show()
            return false
        }

        for (user in listUser) {
            if (username == user.username) {
                return if (password == user.password) {
                    userLoggedIn = user
                    true
                } else {
                    Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
        return false
    }
}