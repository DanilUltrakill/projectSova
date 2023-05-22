package com.projectsova.UI

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.projectsova.R
import com.projectsova.domain.entity.Card
import com.projectsova.databinding.ListofaddressBinding
import java.util.*
import kotlin.collections.ArrayList

class CardAdapter(val cardList: ArrayList<Card>, private val listener: getaddinfoListener ): RecyclerView.Adapter<CardAdapter.CardHolder>() {

    class CardHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = ListofaddressBinding.bind(item)

        @SuppressLint("ResourceAsColor")
        fun bind(card: Card, listener: getaddinfoListener) = with(binding) {
            txtaddress.text = card.address
            checkBox.isChecked = card.arrived
            txtTime.text = card.time
            setBackground(checkBox.isChecked)

            btninfo.setOnClickListener {
                listener.onClick(card)
            }
            checkBox.setOnClickListener {
                getTime(checkBox.isChecked, card)
            }
        }

        private fun setBackground(isChecked: Boolean) {
            if (isChecked) {
                val backgroundColor = ContextCompat.getColor(itemView.context, R.color.arrived)
                binding.cards.setBackgroundColor(backgroundColor)
            } else {
                val backgroundColor = ContextCompat.getColor(itemView.context, R.color.notarrived)
                binding.cards.setBackgroundColor(backgroundColor)
            }
        }

        private fun getTime(isChecked: Boolean, card: Card) {
            if (isChecked) {
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val timeText = timeFormat.format(Date())
                card.time = timeText
                binding.txtTime.text = card.time
            }
            else{
                card.time = ""
                binding.txtTime.text = null
            }
            card.arrived = isChecked
            setBackground(isChecked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listofaddress, parent, false)
        return CardHolder(view)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(cardList[position], listener)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    fun getList(): ArrayList<Card>{
        for (i in cardList.indices){
            cardList[i].id = i
        }
        return cardList
    }

    interface getaddinfoListener{
        fun onClick(card: Card)
    }
}