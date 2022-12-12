package no.ntnu.idatt2506.oving3

import android.app.AlertDialog
import android.content.DialogInterface
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var personArrayList : ArrayList<Person>
    private lateinit var writtenName: EditText
    private lateinit var writtenDateOfBirthday: EditText
    lateinit var submit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        writtenName = findViewById<EditText>(R.id.nameInput)
        writtenDateOfBirthday = findViewById<EditText>(R.id.dateOfBirthdayInput)

        //The date picker pops up when the editText is clicked on
        writtenDateOfBirthday.setOnFocusChangeListener { view, b ->
            datePicker(writtenDateOfBirthday)
        }

        submit = findViewById(R.id.submit)

        personArrayList = ArrayList()
        val list = findViewById<ListView>(R.id.list)
        val adapter: ArrayAdapter<Person> = PersonAdapter(this,personArrayList)
        //Uses ArrayAdapter to fill the list with registered persons (name, dateOfBirthday) from the personArrayList
        list.adapter = adapter

        //Adding a registered person to the listView
        submit.setOnClickListener {
            if (writtenName.text.toString().isNotEmpty() && writtenDateOfBirthday.text.toString().isNotEmpty()) {
                val p = Person(writtenName.text.toString(), writtenDateOfBirthday.text.toString())
                personArrayList.add(p)
                adapter.notifyDataSetChanged()
            }
            else Toast.makeText(this, "Empty input is not allowed", Toast.LENGTH_LONG).show()
        }

        //Person details can be changed by clicking on the person from the listView. Then a dialog box
        // pops up where the preferred details of the selected person can be changed by typing new details
        list.setOnItemClickListener { parent, view, position, id ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Change details")

            val layout = LinearLayout(this)
            val changeNameInput = EditText(this)
            val changeDateOfBirthdayInput = EditText(this)

            changeDateOfBirthdayInput.setOnFocusChangeListener { view, b ->
                datePicker(changeDateOfBirthdayInput)
            }

            changeNameInput.setHint("Change name")
            changeDateOfBirthdayInput.setHint("Change date of birthday")
            layout.orientation = LinearLayout.VERTICAL

            layout.addView(changeNameInput)
            layout.addView(changeDateOfBirthdayInput)
            layout.setPadding(50,40,50,10)

            builder.setView(layout)

            builder.setPositiveButton("CHANGE", DialogInterface.OnClickListener { dialog, which ->
                var newName = changeNameInput.text.toString()
                var newDateOfBirthday = changeDateOfBirthdayInput.text.toString()
                if (newName.isNotEmpty()) personArrayList[position].name = newName
                if (newDateOfBirthday.isNotEmpty()) personArrayList[position].dateOfBirthday = newDateOfBirthday
                adapter.notifyDataSetChanged()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }
    }

    private fun datePicker(date:EditText){
        //Getting the instance of our calendar.
        val c = Calendar.getInstance()

        //Getting day, month and year from the calender.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Creating a variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                //Setting date to the edit text given in the parameter above.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                date.setText(dat)
            },
            year,
            month,
            day
        )
        //Calling show to display our date picker dialog.
        datePickerDialog.show()
    }
}