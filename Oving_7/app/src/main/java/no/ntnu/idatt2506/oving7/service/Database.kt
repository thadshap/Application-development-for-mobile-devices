package no.ntnu.idatt2506.oving7.service

import android.content.Context
import no.ntnu.idatt2506.oving7.managers.DatabaseManager

class Database(context: Context) : DatabaseManager(context){
    val allMovies: ArrayList<String>
        get() = performQuery(TABLE_MOVIES, arrayOf(MOVIE_TITLE))

    val allDirectors: ArrayList<String>
        get() = performQuery(TABLE_DIRECTORS, arrayOf(ID, DIRECTOR_NAME), null)

    val allActors: ArrayList<String>
        get() = performQuery(TABLE_ACTORS, arrayOf(ID, ACTOR_NAME), null)

    val allMoviesAndActors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIES.$MOVIE_TITLE", "$TABLE_ACTORS.$ACTOR_NAME")
            val from = arrayOf(TABLE_MOVIES, TABLE_ACTORS, TABLE_MOVIE_ACTOR)
            val join = JOIN_MOVIE_ACTOR

            return performRawQuery(select, from, join)
        }

    val getAllMoviesAndTheirDirectors: ArrayList<String>
        get() {
            val select = arrayOf("$TABLE_MOVIES.$MOVIE_TITLE", "$TABLE_DIRECTORS.$DIRECTOR_NAME")
            val from = arrayOf(TABLE_MOVIES, TABLE_DIRECTORS)
            val join = JOIN_MOVIE_DIRECTOR

            return performRawQuery(select, from, join)
        }

    fun getAllActorsFromAMovie(title: String): ArrayList<String>{
        val select = arrayOf("$TABLE_ACTORS.$ACTOR_NAME")
        val from = arrayOf(TABLE_MOVIES, TABLE_ACTORS, TABLE_MOVIE_ACTOR)
        val join = JOIN_MOVIE_ACTOR
        val where = "$TABLE_MOVIES.$MOVIE_TITLE='$title'"

        return performRawQuery(select, from, join, where)
    }
}