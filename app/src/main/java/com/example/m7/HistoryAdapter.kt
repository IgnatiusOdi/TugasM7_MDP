package com.example.m7

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.*

class HistoryAdapter(
    private val data: MutableList<HistoryEntity>,
    private val status: Int,
): RecyclerView.Adapter<HistoryAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(itemView.inflate(
            R.layout.layout_history, parent ,false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = data[position]
        val number = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.tvKeterangan.text = item.keterangan
        holder.tvNominal.text = "${number.format(item.nominal)},00"
        if (status == -1) {
            holder.tvNominal.setTextColor(Color.RED)
        } else {
            holder.tvNominal.setTextColor(Color.parseColor("#FF018786"))
        }
        holder.tvSumber.text = "Sumber : ${item.bank_nama}"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan_history)
        val tvNominal: TextView = itemView.findViewById(R.id.tvNominal_history)
        val tvSumber: TextView = itemView.findViewById(R.id.tvSumber_history)
    }
}