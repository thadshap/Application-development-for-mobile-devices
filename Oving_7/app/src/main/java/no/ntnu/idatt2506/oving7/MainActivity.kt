package no.ntnu.idatt2506.oving7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import no.ntnu.idatt2506.oving7.databinding.ActivityMainBinding
import no.ntnu.idatt2506.oving7.managers.FileManager
import no.ntnu.idatt2506.oving7.managers.MyPreferenceManager
import no.ntnu.idatt2506.oving7.service.Database
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var minLayout: ActivityMainBinding
    private lateinit var fileManager: FileManager
    private lateinit var database: Database
    private lateinit var myPreferenceManager: MyPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        minLayout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(minLayout.root)

        database = Database(this)
        fileManager = FileManager(this)
        database.clear()

        myPreferenceManager = MyPreferenceManager(this)
        changeBC()

        var content = ""

        val movies = fileManager.readFileFromResFolder(R.raw.movies_details).split(";\n")
        movies.forEach {
            val movieDetails = it.lines()
            database.insert(movieDetails[0],movieDetails[1],movieDetails[2].split(","))
            content += "$it\n"

        }
        fileManager.write(content)
    }

    private fun showResults(list: ArrayList<String>) {
        val res = StringBuffer("")
        for (s in list) res.append("$s\n")
        minLayout.result.text = res
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        menu.add(0, 1, 0, "Alle filmer")
        menu.add(0, 2, 0, "Alle regissører")
        menu.add(0, 3, 0, "Alle skuspillerene")
        menu.add(0, 4, 0, "Alle filmer og skuspillere")
        menu.add(0, 5, 0, "Alle filmer og regissører")
        menu.add(0, 6, 0, "Skuspillerene fra Top Gun: Maverick")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent("no.ntnu.idatt2506.oving7.SettingsActivity")
                resultLauncher.launch(intent)
            }
            1             -> showResults(database.allMovies)
            2             -> showResults(database.allDirectors)
            3             -> showResults(database.allActors)
            4             -> showResults(database.allMoviesAndActors)
            5             -> showResults(database.getAllMoviesAndTheirDirectors)
            6             -> showResults(database.getAllActorsFromAMovie("Top Gun: Maverick"))
            else          -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        changeBC()
    }

    private fun changeBC(){
        myPreferenceManager.changeBackgroundColor()
    }
}