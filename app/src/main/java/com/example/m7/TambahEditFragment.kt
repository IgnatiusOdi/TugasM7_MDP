package com.example.m7

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat

class TambahEditFragment(
    val indexUser: Int,
    val action: String,
    val indexHistory: Int? = null,
) : Fragment() {
    lateinit var btPengeluaran: Button
    lateinit var btPemasukan: Button
    lateinit var spinnerJenis: Spinner
    lateinit var etKeterangan: EditText
    lateinit var etJumlah: EditText
    lateinit var etTanggal: EditText
    lateinit var btAction: Button

    private val coroutine = CoroutineScope(Dispatchers.IO)
    var status = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah_edit, container, false)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btPengeluaran = view.findViewById(R.id.btPengeluaran_tambahedit)
        btPemasukan = view.findViewById(R.id.btPemasukan_tambahedit)
        spinnerJenis = view.findViewById(R.id.spinnerJenis_tambahedit)
        etKeterangan = view.findViewById(R.id.etKeterangan_tambahedit)
        etJumlah = view.findViewById(R.id.etJumlah_tambahedit)
        etTanggal = view.findViewById(R.id.etTanggal_tambahedit)
        btAction = view.findViewById(R.id.btAction_tambahedit)

        spinnerJenis.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, BankEntity.listBank)

        if (action == "tambah") {
            btAction.text = "TAMBAH"
        } else {
            val history = listHistory[indexHistory!!]
            etKeterangan.setText(history.keterangan)
            etJumlah.setText(history.nominal.toString())
            etTanggal.setText(history.tanggal)
            btAction.text = "SIMPAN"
        }

        btPengeluaran.setOnClickListener {
            switchMode(btPengeluaran)
        }

        btPemasukan.setOnClickListener {
            switchMode(btPemasukan)
        }

        btAction.setOnClickListener {
            val jenis = spinnerJenis.selectedItem.toString()
            val keterangan = etKeterangan.text.toString()
            val jumlah = etJumlah.text.toString()

            if (jenis.isBlank() || keterangan.isBlank() || jumlah.isBlank() || etTanggal.text.isBlank()) {
                Toast.makeText(view.context, "Field kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val bank = listBank[spinnerJenis.selectedItemPosition]
                val tanggal = SimpleDateFormat("yyyy/MM/dd").parse(etTanggal.text.toString())
                var trans = true

                if (status == -1) {
                    if (action == "tambah" && bank.saldo - jumlah.toInt() < 0) {
                        trans = false
                    }
                }

                if (trans) {
                    if (action == "tambah") {
                        val sisaSaldo = bank.saldo + (jumlah.toInt() * status)
                        coroutine.launch {
                            db.bankDao.update(BankEntity(bank.id, bank.user_id, bank.name, sisaSaldo))
                            db.historyDao.insert(HistoryEntity(null, bank.id!!, bank.name, keterangan, jumlah.toInt(), tanggal!!.toString(), status))
                            Toast.makeText(view.context, "Berhasil menambahkan transaksi!", Toast.LENGTH_SHORT).show()
                            clearFields()
                        }
                    } else {
                        coroutine.launch {
                            db.historyDao.update(HistoryEntity(indexHistory, bank.id!!, bank.name, keterangan, jumlah.toInt(), tanggal!!.toString(), status))
                            Toast.makeText(view.context, "Berhasil update history!", Toast.LENGTH_SHORT).show()
                            parentFragmentManager
                                .beginTransaction()
                                .replace(R.id.frame_main, HistoryFragment(indexUser))
                                .commit()
                        }
                    }
                } else {
                    Toast.makeText(view.context, "Gagal melakukan transaksi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun switchMode(btn: Button) {
        if (btn == btPengeluaran) {
            status = -1
            btPengeluaran.setBackgroundColor(resources.getColor(R.color.blue_500))
            btPengeluaran.setTextColor(resources.getColor(R.color.white))
            btPemasukan.setBackgroundColor(resources.getColor(R.color.blue_200))
            btPemasukan.setTextColor(resources.getColor(R.color.black))
        } else {
            status = 1
            btPengeluaran.setBackgroundColor(resources.getColor(R.color.blue_200))
            btPengeluaran.setTextColor(resources.getColor(R.color.black))
            btPemasukan.setBackgroundColor(resources.getColor(R.color.blue_500))
            btPemasukan.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun clearFields() {
        etKeterangan.setText("")
        etJumlah.setText("")
        etTanggal.setText("")
    }
}