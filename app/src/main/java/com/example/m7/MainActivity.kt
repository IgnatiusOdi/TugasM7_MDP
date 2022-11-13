package com.example.m7

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.m7.UserEntity.Companion.userLoggedIn
import com.example.m7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var indexUser: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        indexUser = userLoggedIn!!.id!!

        binding.tvUser.text = "Selamat Datang ${userLoggedIn!!.name}!"

        toSaldo()

        binding.navbarMain.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.saldo -> {
                    toSaldo()
                }
                R.id.tambah -> {
                    toTambah()
                }
                else -> {
                    toHistory()
                }
            }
            true
        }
    }

    private fun toSaldo() {
        changeFrame(SaldoFragment(indexUser))
    }

    private fun toTambah() {
        changeFrame(TambahEditFragment(indexUser, "tambah"))
    }

    private fun toHistory() {
        changeFrame(HistoryFragment(indexUser))
    }

    private fun changeFrame(fragment: Fragment) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.frame_main, fragment)
        fragmentManager.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}