package no.ntnu.idatt2506.oving3

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

//Customized adapter to fill a listView with registered persons
class PersonAdapter (private val context: Activity, private val arrayList: ArrayList<Person>)
    : ArrayAdapter<Person>(context,R.layout.list_of_friends,arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_of_friends,null)

        val name: TextView = view.findViewById(R.id.name)
        val dateOfBirthday: TextView = view.findViewById(R.id.dateOfBirthday)

        name.text = arrayList[position].name
        dateOfBirthday.text = arrayList[position].dateOfBirthday

        return view
    }
}