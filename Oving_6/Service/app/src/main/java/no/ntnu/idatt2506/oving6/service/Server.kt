package no.ntnu.idatt2506.oving6.service

import android.widget.TextView
import kotlinx.coroutines.*
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

//adb -s emulator-5554 forward tcp:12345 tcp:12345
//adb device

class Server(private val textView: TextView, private val PORT: Int = 12345) {
    /**
     * When ui is changed, the set() function is called.
     * The set() function ensures that the text that changed ui is displayed on the screen.
     */
    private var ui: String? = ""
        set(str) {
            MainScope().launch { textView.text = str }
            field = str
        }

    /**
     * A list is created to keep track of all client sockets that connect.
     * Then an infinite loop will run to receive all the clients that connect and they will be passed on to the ClientHandler.
     */
    fun start() {
        val clients = arrayListOf<Socket>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ui = "Starting the server ...\n"
                ServerSocket(PORT).use { serverSocket: ServerSocket ->
                    while (true){
                        val clientSocket = serverSocket.accept()
                        clients.add(clientSocket)
                        ui += "Client with port ${clientSocket.port} is connected\n"
                        ClientHandler(clientSocket, clients).start()
                        delay(10000)
                        ui += "Waiting for connection...\n"
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                ui = e.message
            }
        }
    }
}