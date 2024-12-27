package com.example.mycontacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycontacts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactAdapter: ContactAdapter
    private var contactList = mutableListOf<Contact>()
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        // Fetch contacts from the database and update the list
        contactList = databaseHelper.getAllContacts().toMutableList()

        // Setup RecyclerView with proper adapter initialization
        contactAdapter = ContactAdapter(contactList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = contactAdapter

        // FAB for adding contacts
        binding.fab.setOnClickListener {
            showAddContactDialog()
        }

        // Handle empty state visibility
        updateEmptyState()
    }

    private fun showAddContactDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_contact, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.editTextName)
        val phoneEditText = dialogView.findViewById<EditText>(R.id.editTextMobile)
        val cancelImageView = dialogView.findViewById<ImageView>(R.id.arrow_back)
        val saveImageView = dialogView.findViewById<ImageView>(R.id.check)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Contact")
            .setView(dialogView)
            .create()

        cancelImageView.setOnClickListener {
            dialog.dismiss() // Close the dialog when clicking the back arrow
        }

        saveImageView.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()

            // Check if both fields are filled
            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val newContact = Contact(name, phone)

                // Add contact to database
                databaseHelper.addContact(newContact)

                // Add contact to the list and notify the adapter
                contactList.add(newContact)
                contactAdapter.notifyItemInserted(contactList.size - 1)

                // Show a toast to confirm saving
                Toast.makeText(this, "Contact Saved: $name, $phone", Toast.LENGTH_SHORT).show()
                dialog.dismiss() // Close the dialog

                // Update empty state after adding a contact
                updateEmptyState()
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show() // Show the dialog
    }

    private fun updateEmptyState() {
        // Toggle visibility of empty state based on the list size
        if (contactList.isEmpty()) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

}