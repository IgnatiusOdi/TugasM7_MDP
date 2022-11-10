package com.example.m7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.m7.UserEntity.Companion.users
import com.example.m7.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: AppDatabase
    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AppDatabase.db = AppDatabase.build(this)

        coroutine.launch {
            refresh()
        }

        binding.btLoginLogin.setOnClickListener {
            indexUser = loginCheck()
            if (indexUser != -1) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("indexUser", indexUser)
                startActivity(intent)
            }
        }

        binding.btToRegisterLogin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun refresh() {
        users.clear()
        users.addAll(db.userDao.fetch().toMutableList())
    }

    private fun loginCheck(): Int {
        val username = binding.etUsernameLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()

        if (username.isBlank() || password.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this, "Field kosong!", Toast.LENGTH_SHORT).show()
        } else {
            // CEK USER
            for (user in UserEntity.listUser) {
                if (user.username == etUsername.text.toString()) {
                    // CEK PASSWORD
                    return if (user.password == etPassword.text.toString()) {
                        UserEntity.listUser.indexOf(user)
                    } else {
                        // PASSWORD SALAH
                        Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
                        -1
                    }
                }
            }
            // USER TIDAK DITEMUKAN
            Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
        return -1
    }
}