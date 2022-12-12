package no.ntnu.idatt2506.oving_2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RandomNumberGenerator : AppCompatActivity() {
   var limit = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.random_number_generator)

        //Custom definition of limit is used in exercise 2
        limit = intent.getIntExtra("limit", limit)

        generateMultipleRandomNumbers()
    }

    /**
     * Generates six random numbers.
     * The reason why more than one random number is generated is because in exercise 2 two random numbers are needed.
     * But why cannot the method be called twice? Wouldn't it give two random numbers?
     * Yes, we will get two random numbers but it would be the same. The for loop is used to get two/multiple "different" random numbers.
     */
    fun generateMultipleRandomNumbers() {
        val intent = Intent()
        for (i in 0..5){
            intent.putExtra("getRandomNumber$i", (0..limit).random())
        }
        setResult(RESULT_OK, intent) //Sends the intent (generated random numbers) as a result from the RandomNumberGenerator activity
        finish()
    }
}