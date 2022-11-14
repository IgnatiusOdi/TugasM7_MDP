package com.example.m7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.m7.AppDatabase.Companion.db
import com.example.m7.BankEntity.Companion.listBank
import com.example.m7.HistoryEntity.Companion.listHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class ManajemenSaldoFragment(
    private val indexUser: Int
): Fragment() {
    lateinit var etNama: EditText
    lateinit var etSaldo: EditText
    lateinit var btTambah: Button
    lateinit var spinnerJenis: Spinner
    lateinit var etNominal: EditText
    lateinit var btUbah: Button

    private val coroutine = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manajemen_saldo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etNama = view.findViewById(R.id.etNama_manajemen)
        etSaldo = view.findViewById(R.id.etSaldo_manajemen)
        btTambah = view.findViewById(R.id.btTambah_manajemen)
        spinnerJenis = view.findViewById(R.id.spinnerJenis_manajemen)
        etNominal = view.findViewById(R.id.etNominal_manajemen)
        btUbah = view.findViewById(R.id.btUbah_manajemen)

        spinnerJenis.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, listBank)

        spinnerJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                etNominal.setText(listBank[p2].saldo.toString())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        btTambah.setOnClickListener {
            val nama = etNama.text.toString()
            val saldo = etSaldo.text.toString()
            if (nama.isBlank() || saldo.isBlank()) {
                Toast.makeText(view.context, "Field kosong!", Toast.LENGTH_SHORT).show()
            } else {
                coroutine.launch {
                    db.bankDao.insert(BankEntity(null, indexUser, nama, saldo.toInt()))
                }
                activity?.runOnUiThread {
                    Toast.makeText(view.context, "Berhasil menambahkan rekening!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_main, SaldoFragment(indexUser))
                        .commit()
                }
            }
        }

        btUbah.setOnClickListener {
            val jenis = spinnerJenis.selectedItem.toString()
            val nominal = etNominal.text.toString()

            if (jenis.isBlank() && nominal.isBlank()) {
                Toast.makeText(view.context, "Field kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val bank = listBank[spinnerJenis.selectedItemPosition]
                val saldo = bank.saldo
                val tanggal = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                var keterangan = ""
                var status = 1
                val selisih = abs(saldo - nominal.toInt())
                if (nominal.toInt() - saldo > 0) {
                    // PENAMBAHAN
                    keterangan = "Penambahan Saldo"
                    status = 1
                } else if (nominal.toInt() - saldo < 0) {
                    // PENGURANGAN
                    keterangan = "Pengurangan Saldo"
                    status = -1
                }
                coroutine.launch {
                    db.bankDao.update(BankEntity(bank.id, bank.user_id, bank.name, nominal.toInt()))
                    db.historyDao.insert(HistoryEntity(null, bank.id!!, bank.name, keterangan, selisih, tanggal, status))
                }
                activity?.runOnUiThread {
                    Toast.makeText(view.context, "Berhasil mengubah saldo", Toast.LENGTH_SHORT).show()
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_main, SaldoFragment(indexUser))
                        .commit()
                }
            }
        }
    }
}