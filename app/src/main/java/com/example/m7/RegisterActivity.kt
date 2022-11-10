package com.example.m7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirm: EditText
    lateinit var btRegister: Button
    lateinit var btToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName_register)
        etUsername = findViewById(R.id.etUsername_register)
        etPassword = findViewById(R.id.etPassword_register)
        etConfirm = findViewById(R.id.etConfirm_register)
        etPin = findViewById(R.id.etPIN_register)
        btRegister = findViewById(R.id.btRegister_register)
        btToLogin = findViewById(R.id.btToLogin_register)

        btRegister.setOnClickListener {
            if (registerCheck()) {
                // CEK NOREK KEMBAR
                var kembar: Boolean
                var norek: String
                do {
                    kembar = false
                    norek = generateNomorRekening()
                    for (user in UserEntity.listUser) {
                        if (user.norek == norek) {
                            kembar = true
                            break
                        }
                    }
                } while (kembar)
                UserEntity.listUser.add(UserEntity(etName.text.toString(), etUsername.text.toString(), etPassword.text.toString(), etPin.text.toString(), norek, Date()))
                Toast.makeText(this, "Berhasil register!", Toast.LENGTH_SHORT).show()
                clearInput()
            }
        }

        btToLogin.setOnClickListener {
            finish()
        }
    }

    private fun clearInput() {
        etName.setText("")
        etUsername.setText("")
        etPassword.setText("")
        etConfirm.setText("")
        etPin.setText("")
    }

    private fun registerCheck(): Boolean {
        if (etName.text.isBlank() || etUsername.text.isBlank() || etPassword.text.isBlank() || etConfirm.text.isBlank() || etPin.text.isBlank()) {
            // FIELD KOSONG
            Toast.makeText(this, "Field kosong!", Toast.LENGTH_SHORT).show()
            return false
        } else if (etPassword.text.toString() != etConfirm.text.toString()) {
            // PASSWORD != KONFIRMASI
            Toast.makeText(this, "Password dan konfirmasi password harus sama!", Toast.LENGTH_SHORT).show()
            return false
        } else {
            // USERNAME KEMBAR
            for (user in UserEntity.listUser) {
                if (user.username == etUsername.text.toString()) {
                    Toast.makeText(this, "Username tidak boleh kembar!", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }

        return true
    }

    private fun generateNomorRekening(): String {
        var norek = ""
        while (norek.length < 10) {
            norek += randomNumber().toString()
        }
        return norek
    }

    private fun randomNumber(): Int {
        return (0..9).random()
    }

}