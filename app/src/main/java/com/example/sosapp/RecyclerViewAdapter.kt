package com.example.sosapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sosapp.models.Contact


class RecyclerViewAdapter(private val contacts: MutableList<Contact>)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_element,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contact.text = contacts[position].contact_name
        holder.number.text = contacts[position].phone
    }


    override fun getItemCount(): Int {
        return contacts.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val contact: TextView = itemView.findViewById(R.id.tvContact)
        val number: TextView = itemView.findViewById(R.id.tvNumber)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }
}