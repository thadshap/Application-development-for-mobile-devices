package no.ntnu.idatt2506.oving_4

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment



class TitlesFragment : ListFragment(){
    //Declaring empty arrays
    private var titles: Array<String> = arrayOf()
    private var description: Array<String> = arrayOf()
    private var images: TypedArray? = null

    private var callback: Callback? = null

    var currentPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_titles, container, false)

        //Gets the arrays from values/strings and initializing them as values to the declared arrays above
        titles = resources.getStringArray(R.array.titles)
        description = resources.getStringArray(R.array.description)
        images = resources.obtainTypedArray(R.array.images)

        //Uses ArrayAdapter to fill the list with the titles from the titles array
        listAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_list_item_1, android.R.id.text1, titles)
        }
        return view
    }

    /**
     * This method is called  when an item in the list is selected.
     * The position parameter is used to find the position of the vies in the list, so that the associated title, description and image are forwarded with help of showResponse method.
     * The position is also used to set the currentPosition in order to be used in the nextPressed and previousPressed methods.
     */
    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        callback?.showResponse(titles[position],description[position], images?.getDrawable(position))
        currentPosition = position
    }

    /**
     * Interface used to communicate between this and DetailsFragment
     */
    interface Callback {
        fun showResponse(title: String?, description: String?, image: Drawable?)
    }

    /**
     * This method is used to allow this fragment communicate with MainActivity which is an activity. This is done by capture the Callback instance and cast it into the context of the activity.
     * This will be automatically be called as onCreate, onCreateView ect.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = try {
            activity as Callback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                "$activity must implement Callback"
            )
        }
    }

    /**
     * This method will also be called automatically when the fragment is used.
     * Unlike onAttach, this method will be called when it is removed from the FragmentManager and is detached from MainActivity
     */
    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    /**
     * Used in mainActivity to define the next button (send the details needed to display on the DetailsFragment)
     */
    fun nextPressed() {
        if (currentPosition==titles.size-1) currentPosition = 0 //If the user presses next button after selecting the last title in the list, the first title should be displayed
        else if (currentPosition == -1) currentPosition = 0 //This occurs if the user presses the next button without having pressed any of the listed titles. Displays the first title.
        else currentPosition++
        callback!!.showResponse(titles[currentPosition], description[currentPosition], images!!.getDrawable(currentPosition))
    }

    /**
     * Used in mainActivity to define the previous button (send the details needed to display on the DetailsFragment)
     */
    fun previousPressed() {
        if(currentPosition==0) currentPosition = titles.size-1 //If the user presses previous button after selecting the first title in the list, the last title should be displayed
        else if (currentPosition == -1) currentPosition = titles.size-1 //This occurs if the user presses the previous button without having pressed any of the listed titles. Displays the last title.
        else currentPosition--
        callback!!.showResponse(titles[currentPosition], description[currentPosition], images!!.getDrawable(currentPosition))
    }
}