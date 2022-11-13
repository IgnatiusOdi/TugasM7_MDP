package com.example.m7

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.*

class BankAdapter(
    private val data: MutableList<BankEntity>
): RecyclerView.Adapter<BankAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context)
        return CustomViewHolder(itemView.inflate(
            R.layout.layout_bank, parent ,false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = data[position]
        val number = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.tvNama.text = item.name
        holder.tvSaldo.text = "Saldo : ${number.format(item.saldo)},00"
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class CustomViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama_bank)
        val tvSaldo: TextView = itemView.findViewById(R.id.tvSaldo_bank)
    }
}