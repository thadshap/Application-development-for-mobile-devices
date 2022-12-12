package no.ntnu.idatt2506.oving_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class Exercise2 : AppCompatActivity() {
    var number1: Int = 3
    var number2: Int = 5
    var limit: Int = 10

    //Declaring empty View variables
    private var number1TextView: TextView? = null
    private var number2TextView: TextView? = null
    private var answerEditView: EditText? = null
    private var limitEditView: EditText? = null
    private var limitTextView: TextView? = null
    private var answerTextView: TextView? = null

    //Gets two random number if the result is RESULT_OK. Then the random numbers is displayed on the screen as textViews (id = number1 & id =number2)
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    if (result.data != null) {
                        val randomNumber1 = result.data!!.getIntExtra("getRandomNumber1", number1)
                        val randomNumber2 = result.data!!.getIntExtra("getRandomNumber2", number2)
                        number1 = randomNumber1
                        number2 = randomNumber2
                        number1TextView?.text = "Tall 1: $number1"
                        number2TextView?.text = "Tall 2: $number2"
                    }
                }
                else -> {
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercise_2)

        //Declaring the empty variables to views from exercise_2 layout
        number1TextView = findViewById<TextView>(R.id.number1)
        number2TextView = findViewById<TextView>(R.id.number2)
        answerEditView = findViewById<EditText>(R.id.answer)
        limitEditView = findViewById<EditText>(R.id.limit)
        limitTextView = findViewById<TextView>(R.id.textLimit)
        answerTextView = findViewById<TextView>(R.id.textAnswer)

        //Sets default values
        number1TextView?.text = "Tall 1: $number1"
        number2TextView?.text = "Tall 2: $number2"
        limitEditView?.setText("$limit")

        limitTextView?.text ="${getString(R.string.limit)} $limit"
    }

    /**
     * Method for the add button.
     * Calculates the answer and sets the correct answer in a textView.
     * Then the answer is  compared with the written answer.
     * Lastly a message is displayed regarding whether the written answer is correct or incorrect.
     */
    fun addBtn(v: View){
        val correctAnswer = number1 + number2
        val writtenAnswer = Integer.parseInt(answerEditView?.text.toString())
        answerTextView?.text ="${getString(R.string.answer)} $correctAnswer"
        if(correctAnswer==writtenAnswer) formater(getString(R.string.correctAnswer))
        else formater("${getString(R.string.wrongAnswer)} $correctAnswer")
    }

    /**
     * Method for the multiply button.
     * Calculates the answer and sets the correct answer in a textView.
     * Then the answer is  compared with the written answer.
     * Lastly a message is displayed regarding whether the written answer is correct or incorrect.
     */
    fun multiplyBtn(v: View){
        val correctAnswer = number1 * number2
        val writtenAnswer = Integer.parseInt(answerEditView?.text.toString())
        answerTextView?.text ="${getString(R.string.answer)} $correctAnswer"
        if(correctAnswer==writtenAnswer) formater(getString(R.string.correctAnswer))
        else formater("${getString(R.string.wrongAnswer)} $correctAnswer")
    }

    /**
     * Displays a toggle with the message from the parameter msg.
     * Then the selected limit for generating two random numbers is sent and set in a textView.
     */
    fun formater(msg : String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        limit = Integer.parseInt(limitEditView?.text.toString())
        val intent = Intent(this, RandomNumberGenerator::class.java)
        intent.putExtra("limit", limit)
        limitTextView?.text ="${getString(R.string.limit)} $limit"
        getResult.launch(intent)
    }
}