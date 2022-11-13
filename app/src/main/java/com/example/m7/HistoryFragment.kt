package com.example.m7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.m7.HistoryEntity.Companion.listHistory

class HistoryFragment(
    val indexUser: Int
) : Fragment() {
    lateinit var spinnerTanggal: Spinner
    lateinit var spinnerJenis: Spinner
    lateinit var rv: RecyclerView
    lateinit var tvPengeluaran: TextView
    lateinit var tvPemasukan: TextView

    lateinit var historyAdapter: HistoryAdapter

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

//        spinnerTanggal = Ar
//        historyAdapter = HistoryAdapter(listHistory, )
        rv.addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
        rv.adapter = historyAdapter
        rv.layoutManager = LinearLayoutManager(view.context)

        refreshSummary()

        spinnerJenis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    fun refreshSummary() {

    }
}