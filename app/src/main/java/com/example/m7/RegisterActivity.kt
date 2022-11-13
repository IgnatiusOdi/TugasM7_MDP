package com.example.m7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.m7.AppDatabase.Companion.db
import com.example.m7.UserEntity.Companion.listUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirm: EditText
    lateinit var btRegister: Button
    lateinit var btToLogin: Button

    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName_register)
        etUsername = findViewById(R.id.etUsername_register)
        etPassword = findViewById(R.id.etPassword_register)
        etConfirm = findViewById(R.id.etConfirm_register)
        btRegister = findViewById(R.id.btRegister_register)
        btToLogin = findViewById(R.id.btToLogin_register)

        btRegister.setOnClickListener {
            registerCheck()
        }

        btToLogin.setOnClickListener {
            finish()
        }
    }

    private suspend fun refreshListUser() {
        listUser.clear()
        listUser.addAll(db.userDao.fetch().toMutableList())
    }

    private fun clearInput() {
        etName.setText("")
        etUsername.setText("")
        etPassword.setText("")
        etConfirm.setText("")
    }

    private fun registerCheck() {
        val name = etName.text.toString()
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val confirm = etConfirm.text.toString()

        if (name.isBlank() || username.isBlank() || password.isBlank() || confirm.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this, "Field kosong!", Toast.LENGTH_SHORT).show()
            return
        } else if (password != confirm) {
            // PASSWORD != KONFIRMASI
            Toast.makeText(this, "Password dan konfirmasi password harus sama!", Toast.LENGTH_SHORT).show()
            return
        } else {
            for (user in listUser) {
                if (username == user.username) {
                    Toast.makeText(this, "Username tidak boleh kembar!", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }

        coroutine.launch {
            db.userDao.insert(UserEntity(null, name, username, password))
            refreshListUser()
            runOnUiThread {
                Toast.makeText(this@RegisterActivity, "Berhasil register!", Toast.LENGTH_SHORT).show()
                clearInput()
            }
        }
    }
}