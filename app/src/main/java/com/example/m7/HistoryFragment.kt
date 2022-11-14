package com.example.m7

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.m7.AppDatabase.Companion.db
import com.example.m7.BankEntity.Companion.listBank
import com.example.m7.HistoryEntity.Companion.listHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment(
    val indexUser: Int
) : Fragment() {
    lateinit var spinnerTanggal: Spinner
    lateinit var spinnerJenis: Spinner
    lateinit var rv: RecyclerView
    lateinit var tvPengeluaran: TextView
    lateinit var tvPemasukan: TextView

    private val coroutine = CoroutineScope(Dispatchers.IO)
    lateinit var historyAdapter: HistoryAdapter
    var filterHistory: ArrayList<HistoryEntity> = arrayListOf()
    var listTanggal: ArrayList<String> = arrayListOf()
    var status = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerTanggal = view.findViewById(R.id.spinnerTanggal_history)
        spinnerJenis = view.findViewById(R.id.spinnerJenis_history)
        rv = view.findViewById(R.id.rv_history)
        tvPengeluaran = view.findViewById(R.id.tvPengeluaran_history)
        tvPemasukan = view.findViewById(R.id.tvPemasukan_history)

        coroutine.launch {
            refreshListHistory()
        }

        refreshTanggal()
        spinnerTanggal.adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, listTanggal)

        historyAdapter = HistoryAdapter(filterHistory, status)
        rv.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        rv.adapter = historyAdapter
        rv.layoutManager = LinearLayoutManager(view.context)

        searchHistory()
        refreshSummary()

        historyAdapter.onClickListener = fun (it: View, position: Int, history: HistoryEntity) {
            val popUp = PopupMenu(view.context, it)
            popUp.menuInflater.inflate(R.menu.popup, popUp.menu)
            popUp.setOnMenuItemClickListener {
                return@setOnMenuItemClickListener when(it.itemId){
                    R.id.delete -> {
                        var bank: BankEntity
                        if (history.status == 1) {
                            coroutine.launch {
                                bank = db.bankDao.getBank(history.bank_id)!!
                                db.bankDao.update(BankEntity(history.bank_id, indexUser, history.bank_nama, bank.saldo - history.nominal))
                                db.historyDao.delete(history)
                            }
                        } else {
                            coroutine.launch {
                                bank = db.bankDao.getBank(history.bank_id)!!
                                db.bankDao.update(BankEntity(history.bank_id, indexUser, history.bank_nama, bank.saldo + history.nominal))
                                db.historyDao.delete(history)
                            }
                        }
                        activity?.runOnUiThread {
                            Toast.makeText(view.context, "Berhasil menghapus history!", Toast.LENGTH_SHORT).show()
                            parentFragmentManager
                                .beginTransaction()
                                .replace(R.id.frame_main, SaldoFragment(indexUser))
                                .commit()
                        }
                        true
                    }
                    R.id.edit -> {
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.frame_main, TambahEditFragment(indexUser, "edit", history.id))
                            .commit()
                        true
                    }
                    else->{
                        false
                    }
                }
            }
            popUp.show()
        }

        spinnerTanggal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                searchHistory()
                refreshSummary()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spinnerJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                searchHistory()
                refreshSummary()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private suspend fun refreshListHistory() {
        listHistory.clear()
        for (bank in listBank) {
            listHistory.addAll(db.historyDao.get(bank.id!!).toMutableList())
        }
    }

    fun searchHistory() {
        if (spinnerTanggal.selectedItemPosition != -1) {
            filterHistory.clear()
            var jenis = -1
            if (spinnerJenis.selectedItemPosition == 1) {
                jenis = 1
            }
            for (history in listHistory) {
                if (history.tanggal == spinnerTanggal.selectedItem.toString() && history.status == jenis) {
                    filterHistory.add(history)
                }
            }
            historyAdapter.notifyDataSetChanged()
        }
    }

    fun refreshTanggal() {
        listTanggal.clear()
        for (history in listHistory) {
            if (!listTanggal.contains(history.tanggal)) {
                listTanggal.add(history.tanggal)
            }
        }
        listTanggal.sort()
    }

    @SuppressLint("SetTextI18n")
    fun refreshSummary() {
        val number = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        var total = 0
        for (history in filterHistory) {
            total += history.nominal
        }
        if (spinnerJenis.selectedItemPosition == 0) {
            tvPengeluaran.text = "${number.format(total)},00"
            tvPemasukan.text = "Rp0,00"
        } else {
            tvPengeluaran.text = "Rp0,00"
            tvPemasukan.text = "${number.format(total)},00"
        }
    }
}