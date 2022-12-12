package no.ntnu.idatt2506.httpandcoroutines.guessinggame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GuessingGameActivity : AppCompatActivity() {

    private val network: HttpWrapper = HttpWrapper(URL)
    private lateinit var name: String
    private lateinit var cardNumber: String
    private var count : Int = 0  // Counts the number of times the user has guessed.
    private var displayResponse: String = "" // Takes care of the text obtained from the server.
    private var hasWon: Boolean = false // When the user has won, this variable changes to true.
    private lateinit var newGameBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guessing_game)

        // Initializes the "new game" button.
        newGameBtn = findViewById(R.id.newGame)

        // Gets name and card number from MainActivity and initializes name and card number with these values.
        name = intent.getStringExtra("name").toString()
        cardNumber = intent.getStringExtra("cardNumber").toString()

        // Performs an HTTP request by calling the performRequest method with the parameters name and card number.
        performRequest(
            mapOf(
            "navn" to name,
            "kortnummer" to cardNumber,
        ))
        Log.i("MainActivity: name", name)
        Log.i("MainActivity: card.nr", cardNumber)
    }

    /**
     * The logic behind the guess number button that is pressed after the user has entered a number.
     * The number is sent to the server using the performRequest method.
     * The button to restart the game becomes visible when the user has either guessed three times or when the user has won.
     */
    fun onClickGuessNumber(v: View?){
        val guess = findViewById<EditText>(R.id.guessNumber).text.toString()
        performRequest(
            mapOf(
            "tall" to guess,
        ))
        Log.i("guessed number", guess)
        count ++
        if (count == 3 || hasWon) {
            newGameBtn.isEnabled = true
        }
    }

    /**
     * Makes an HTTP request separately from the main thread.
     */
    private fun performRequest(parameterList: Map<String, String>) {
        CoroutineScope(IO).launch {
            val response: String = try {
                network.get(parameterList)
            } catch (e: Exception) {
                Log.e("performRequest()", e.toString())
                e.toString()

            }
            Log.i("Response from server", response)

            MainScope().launch {
                setResult(response)
            }
        }
    }

    /**
     * Changes the UI text in connection with the text that is sent from the server to the client.
     */
    private fun setResult(response : String){
        displayResponse = response.replace("\n", "").replace("null", "")
        findViewById<TextView>(R.id.guessNumberText).text = displayResponse
        // Checks if the user has won by seeing if the text sent from the server contains "vunnet".
        // The result is stored as a boolean in the hasWon variable which is further used in the onClickGuessNumber method to determine when the replay button should be displayed.
        hasWon = displayResponse.contains("vunnet")
    }

    /**
     * When the button to play again is pressed, the user is sent to the login page.
     */
    fun onClickNewGame(v: View?){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}