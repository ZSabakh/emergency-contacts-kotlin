package com.example.sosapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.ui.models.ContactUIModel


class ContactsRecyclerViewAdapter(private val contactUIModels: List<ContactUIModel>) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_element, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isSelected = false;
        holder.contact.text = contactUIModels[position].contactName
        holder.number.text = contactUIModels[position].phone
        holder.itemView.setOnClickListener {
            isSelected = !isSelected
            contactUIModels[position].onClick()

            if (isSelected) {
                holder.cvContactElement.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.green_700))
            } else {
                holder.cvContactElement.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.green_500))
            }
        }
    }


    override fun getItemCount(): Int {
        return contactUIModels.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contact: TextView = itemView.findViewById(R.id.tv_contact)
        val number: TextView = itemView.findViewById(R.id.tv_number)
        val cvContactElement: CardView = itemView.findViewById(R.id.cv_contact_element)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}