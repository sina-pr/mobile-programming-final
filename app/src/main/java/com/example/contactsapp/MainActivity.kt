package com.example.contactsapp

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val REQUEST_CONTACT = 201
    private fun FetchPhoneNumber (){
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_CONTACT)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val openContactBtn = findViewById<Button>(R.id.btn_contact)

        openContactBtn.setOnClickListener{
            FetchPhoneNumber()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvPhoneNumber = findViewById<TextView>(R.id.tv_phone_number)
        val tvId = findViewById<TextView>(R.id.tv_id)
        if (requestCode == REQUEST_CONTACT && resultCode== Activity.RESULT_OK ) {
            val contactUri = data?.getData()

            val contactCursor= managedQuery(contactUri , null, null, null, null);
            contactCursor.moveToFirst()
            var contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID))
            var contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            var hasPhoneNum = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))


            if(hasPhoneNum=="1"){
                val crPhones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + " = ?", arrayOf(contactId), null)
                crPhones?.moveToFirst()
                var phoneNo = crPhones?.getString(
                    crPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                tvId.text = contactId
                tvName.text=contactName
                tvPhoneNumber.text=phoneNo


            }

        }
    }
}