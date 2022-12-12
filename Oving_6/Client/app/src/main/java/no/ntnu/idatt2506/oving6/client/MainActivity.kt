package no.ntnu.idatt2506.oving6.client

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val received = findViewById<TextView>(R.id.textView)
        val sent = findViewById<EditText>(R.id.message)
        val sendBtn = findViewById<Button>(R.id.sendBtn)
        Client(received,sent,sendBtn).start()
    }
}