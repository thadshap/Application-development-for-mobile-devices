package no.ntnu.idatt2506.oving6.service

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.*
import java.net.Socket

class ClientHandler(private val clientSocket: Socket, private val clients: ArrayList<Socket>) {
    /**
     * Uses the readFromClient function to read the message sent by a client and forwards that message to the other clients using the sendToOtherClients function.
     */
    fun start(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                clientSocket.use {
                    readFromClient(clientSocket) { message: String ->
                        val from = clientSocket.port
                        val sendMsg = "Client from port $from: $message"
                        Log.i("Msg sent from server", message)
                        sendToOtherClients(sendMsg)
                    }
                }
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    /**
     * Sends the message that one client sent to all the other connected clients.
     */
    private fun sendToOtherClients(message: String){
        for(socket in clients){
            if(socket != clientSocket){
                val writer = PrintWriter(socket.getOutputStream(), true)
                writer.println(message)
            }
        }
    }

    /**
     * There is an infinite loop that runs to read all messages sent by the clients.
     */
    companion object {
        fun readFromClient(socket: Socket, callback: (message: String) -> Unit){
            CoroutineScope(IO).let {
                while (true){
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                    val message = reader.readLine()
                    if(message != null) callback(message)
                }
            }
        }
    }
}