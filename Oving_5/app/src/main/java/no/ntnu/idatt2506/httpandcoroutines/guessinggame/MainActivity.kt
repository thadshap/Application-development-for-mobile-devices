package no.ntnu.idatt2506.httpandcoroutines.guessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

// Server
const val URL = "https://bigdata.idi.ntnu.no/mobil/tallspill.jsp"

class MainActivity : AppCompatActivity() {
    private val network: HttpWrapper = HttpWrapper(URL)
    private lateinit var name: String
    private lateinit var cardNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Initializes the name and card number variables to what the user has typed.
     * If the card number has been entered, the name and card number information will be forwarded to GuessingGameActivity.
     * If not, only the name will be sent as a parameter to the HTTP request to the server to get feedback that the card number must also be given.
     */
    fun onClickLogIn() {
        name = findViewById<EditText>(R.id.name).text.toString()
        cardNumber = findViewById<EditText>(R.id.cardNumber).text.toString()
        Log.i("Client: name", name)
        Log.i("Client: card.nr", cardNumber)
        if (cardNumber.isNotEmpty())   {
            val intent = Intent("GuessingGameActivity")
            intent.putExtra("name", findViewById<EditText>(R.id.name).text.toString())
            intent.putExtra("cardNumber", findViewById<EditText>(R.id.cardNumber).text.toString())
            startActivity(intent)
        } else performRequest(
            mapOf(
            "navn" to name,
        ))
    }

    /**
     * Makes an HTTP request separately from the main thread.
     */
    private fun performRequest(parameterList: Map<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val response: String = try {
                network.get(parameterList)
            } catch (e: Exception) {
                e.toString()
            }
            Log.i("Server: after log in", response)

            MainScope().launch {
                setResult(response)
            }
        }
    }

    /**
     * Changes the UI text in connection with the text that is sent from the server to the client.
     */
    private fun setResult (response: String?){
        val displayResponse = response?.replace("\n", "")?.replace("null", "")
        findViewById<TextView>(R.id.failLogIn).text = displayResponse
    }
}