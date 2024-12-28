package com.example.mycontacts
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
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


    override fun getItemCount(): Int {
        return contactList.size
    }
    private fun makePhoneCall(phoneNumber: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, make the call
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
        } else {
            // Permission not granted, show a message
            Toast.makeText(context, "Permission denied. Enable CALL_PHONE permission.", Toast.LENGTH_SHORT).show()
        }
    }
}
