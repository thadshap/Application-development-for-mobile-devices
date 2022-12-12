package no.ntnu.idatt2506.oving6.client

import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(
    private val received: TextView,
    private val sent: EditText,
    private val sendBtn: Button,
    private val SERVER_IP: String = "10.0.2.2",
    private val SERVER_PORT: Int = 12345,
) {
    /**
     * When ui is changed, the set() function is called.
     * The set() function ensures that the text that changed ui is displayed on the screen.
     */
    private var ui: String? = ""
        set(str) {
            MainScope().launch { received.text = str }
            field = str
        }

    /**
     * Connecting to the server.
     * Listens for when the client presses the send message button and if the button is pressed, the sendMessage function is called.
     * Uses the function readFromServer to get the messages that other clients sent and displays it on the screen.
     */
    fun start() {
        CoroutineScope(IO).launch {
            ui += "Connecting to...\n"
            try {
                Socket(SERVER_IP, SERVER_PORT).let { socket: Socket ->
                    ui += "Connected to server with port ${socket.port} \n"
                    sendBtn.setOnClickListener {
                        sendMessage(socket)
                    }
                    readFromServer(socket) { message: String ->
                        Log.i("Message from server", message)
                        ui += message + "\n"
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                ui = e.message
            }
        }
    }

    /**
     * Sends the message that the client sent to the server
     */
    private fun sendMessage(socket: Socket) {
        CoroutineScope(IO).launch {
            val message = sent.text.toString()
            val writer = PrintWriter(socket.getOutputStream(), true)
            writer.println(message)
            ui += "Me: $message\n"
        }
    }

    /**
     * It is an infinite loop that runs to read messages that are sent from the clients through the server.
     */
    companion object {
        fun readFromServer(socket: Socket, callback: (message: String) -> Unit){
            CoroutineScope(IO).let {
                while (true){
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val message = reader.readLine()
                    if(message != null) callback(message)
                    else break
                }
            }
        }
    }
}