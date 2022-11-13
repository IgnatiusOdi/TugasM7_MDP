package com.example.m7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.m7.BankEntity.Companion.listBank
import com.example.m7.HistoryEntity.Companion.listHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class SaldoFragment(
    private val indexUser: Int
) : Fragment() {
    lateinit var tvPengeluaran: TextView
    lateinit var tvSaldo: TextView
    lateinit var rv: RecyclerView
    lateinit var btManajemenSaldo: Button

    lateinit var bankAdapter: BankAdapter
    private val coroutine = CoroutineScope(Dispatchers.IO)
    val number = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saldo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPengeluaran = view.findViewById(R.id.tvPengeluaran_saldo)
        tvSaldo = view.findViewById(R.id.tvSaldo_saldo)
        rv = view.findViewById(R.id.rv_saldo)
        btManajemenSaldo = view.findViewById(R.id.btManajemenSaldo_saldo)

        coroutine.launch {
            refreshListBank()
            refreshListHistory()
            refreshTotalPengeluaran()
            refreshTotalSaldo()
        }

        bankAdapter = BankAdapter(listBank)
        rv.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        rv.adapter = bankAdapter
        rv.layoutManager = LinearLayoutManager(view.context)

        btManajemenSaldo.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.frame_main, ManajemenSaldoFragment(indexUser))
                .commit()
        }
    }

    private suspend fun refreshListBank() {
        listBank.clear()
        listBank.addAll(AppDatabase.db.bankDao.get(indexUser).toMutableList())
    }

    private suspend fun refreshListHistory() {
        listHistory.clear()
        for (bank in listBank) {
            listHistory.addAll(AppDatabase.db.historyDao.get(bank.id!!).toMutableList())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshTotalPengeluaran() {
        var total = 0
        for (history in listHistory) {
            if (history.status == -1) {
                total += history.nominal
            }
        }
        tvPengeluaran.text = "${number.format(total)},00"
    }

    @SuppressLint("SetTextI18n")
    private fun refreshTotalSaldo() {
        var total = 0
        for (bank in listBank) {
            total += bank.saldo
        }
        tvSaldo.text = "${number.format(total)},00"
    }
}