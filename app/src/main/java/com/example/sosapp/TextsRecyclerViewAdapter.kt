package com.example.sosapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.ui.models.TextUIModel


class TextsRecyclerViewAdapter(private val textUIModels: List<TextUIModel>)
    : RecyclerView.Adapter<TextsRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_element,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = textUIModels[position].text
        holder.itemView.setOnClickListener{
            textUIModels[position].onClick()
        }
        if(textUIModels[position].isSelected){
            holder.cvTextElement.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.green_700))
        }else if(textUIModels[position].isAdmin){
            holder.cvTextElement.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.green_400))
        }else{
            holder.cvTextElement.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.green_500))
        }
    }


    override fun getItemCount(): Int {
        return textUIModels.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val text: TextView = itemView.findViewById(R.id.tv_text)
        val cvTextElement: CardView = itemView.findViewById(R.id.cv_text_element)

    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}