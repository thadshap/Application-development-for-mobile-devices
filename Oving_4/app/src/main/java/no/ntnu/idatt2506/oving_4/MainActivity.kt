package no.ntnu.idatt2506.oving_4

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.GridView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), TitlesFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOrientation(resources.configuration) //This runs in the beginning when the activity starts. Calls setOrientation method and places the fragments in relation to the orientation of the screen
    }

    /**
     * This method will receive a call when a change in one of the configurations declared in the manifest occurs.
     * With other words, if any changes occur on the screen orientation or screen size (android:configChanges="orientation|screenSize"), then this method will be called.
     *
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setOrientation(newConfig)
    }

    /**
     * This method takes configuration as parameter. The configuration is used to find the orientation of the device.
     * When the screen is oriented in portrait display, the image and description will be below the list, while when
       the screen is horizontal, the image and description are placed next to the list.
     */
    private fun setOrientation(config: Configuration) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val content = findViewById<GridView>(R.id.mainActicity) as LinearLayout
        content.orientation =
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayout.VERTICAL
            else LinearLayout.HORIZONTAL
        transaction.commit()
    }

    /**
     * Communication between TitlesFragment and DetailsFragment.
     * Title, description and image is sent from TitlesFragment and that information is received here to then be passed on to DetailsFragment.
     */
    override fun showResponse(title: String?, description: String?, image: Drawable?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.detailsFragment) as DetailsFragment
        fragment.setDescription(title, description, image)
    }

    /**
     * Calls the nextPressed method TitlesFragment since it is the one that has the logic for handling the next button.
     */
    private fun nextPressed(){
        val fragment = supportFragmentManager.findFragmentById(R.id.titlesFragment) as TitlesFragment
        fragment.nextPressed()
    }

    /**
     * Calls the previousPressed method TitlesFragment since it is the one that has the logic for handling the previous button.
     */
    private fun previousPressed(){
        val fragment = supportFragmentManager.findFragmentById(R.id.titlesFragment) as TitlesFragment
        fragment.previousPressed()
    }

    /**
     * Uses the layout created under menu/menu_next_prev and adds the menu to MainActivity
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_next_prev, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Gives the menu options next and previous logic by linking them to the methods nextPressed and previousPressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_prev -> previousPressed()
            R.id.menu_next -> nextPressed()
            else -> return false
        }
        return true
    }
}