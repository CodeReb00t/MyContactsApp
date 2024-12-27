package com.example.mycontacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycontacts.databinding.ItemContactBinding

class ContactAdapter(private var contactList: MutableList<Contact>, private val context: Context) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.binding.contactName.text = contact.name
        holder.binding.contactPhone.text = contact.phoneNumber

        // Set up the phone call icon click listener
        holder.binding.callIcon.setOnClickListener {
            val phoneNumber = contact.phoneNumber
            makePhoneCall(phoneNumber)
        }
    }

    private fun makePhoneCall(phoneNumber: String) {
        // Create the intent to dial the phone number
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")

        // Use the context to start the activity
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}
