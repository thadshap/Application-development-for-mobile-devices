package no.ntnu.idatt2506.oving_4

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class DetailsFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)

    }

    /**
     * Gets title, description and image from MainActivity, which are then set in the fragment_details layout
     */
    fun setDescription(title: String?, description: String?, image: Drawable?){
        requireView().findViewById<TextView>(R.id.title).text = title
        requireView().findViewById<TextView>(R.id.description).text = description
        requireView().findViewById<ImageView>(R.id.imageView).setImageDrawable(image)

    }

}