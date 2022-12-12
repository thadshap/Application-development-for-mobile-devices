package no.ntnu.idatt2506.oving_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class Exercise1 : AppCompatActivity() {

    //Gets the random number if the result is RESULT_OK. Then the random number is displayed on the screen as a textView (id = exercise_1_text_result)
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { value ->
            when (value.resultCode) {
                RESULT_OK -> {
                    if (value.data != null) {
                        val generatedNumber = value.data!!.getIntExtra("getRandomNumber1", -1)
                        Toast.makeText(this, "Random number: $generatedNumber", Toast.LENGTH_LONG).show()
                        val textViewRandomNumber = findViewById<TextView>(R.id.exercise_1_text_result)
                        textViewRandomNumber.text = "$generatedNumber"
                    }
                }
                else -> {}
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_1)
    }

    /**
     * Defines the button click for the button randomNumberGeneratorButton (displays a random number)
     */
    fun onClickRandomNumberActivity(v: View?) {
        getResult.launch(Intent(this, RandomNumberGenerator::class.java))
    }
}

