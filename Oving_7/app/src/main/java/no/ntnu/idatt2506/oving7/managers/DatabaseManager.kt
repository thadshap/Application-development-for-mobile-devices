package no.ntnu.idatt2506.oving7.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DatabaseManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {

        const val DATABASE_NAME = "MovieDatabase"
        const val DATABASE_VERSION = 1

        const val ID = "_id"

        const val TABLE_MOVIES = "MOVIES"
        const val MOVIE_TITLE = "title"
        const val MOVIE_DIRECTOR_ID = "director_movie_id"

        const val TABLE_DIRECTORS = "DIRECTORS"
        const val DIRECTOR_NAME = "director"

        const val TABLE_ACTORS = "ACTORS"
        const val ACTOR_NAME = "actor"

        const val TABLE_MOVIE_ACTOR = "MOVIE_ACTOR"
        const val MOVIE_ID = "title_id"
        const val ACTOR_ID = "actor_id"

        // Many-to-many
        // A film can consist of many actors and an actor can have appeared in many films
        val JOIN_MOVIE_ACTOR = arrayOf(
            "$TABLE_MOVIES.$ID=$TABLE_MOVIE_ACTOR.$MOVIE_ID",
            "$TABLE_ACTORS.$ID=$TABLE_MOVIE_ACTOR.$ACTOR_ID"
        )

        // One-to-many
        // Many films can be directed by the same director, but a film can only have one director
        val JOIN_MOVIE_DIRECTOR = arrayOf(
            "$TABLE_DIRECTORS.$ID=$TABLE_MOVIES.$MOVIE_DIRECTOR_ID",
        )

    }

    /**
     *  Create the tables
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """create table $TABLE_DIRECTORS (
						$ID integer primary key autoincrement, 
						$DIRECTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_MOVIES (
						$ID integer primary key autoincrement, 
						$MOVIE_TITLE text unique not null,
                        $MOVIE_DIRECTOR_ID numeric,
                        FOREIGN KEY($MOVIE_DIRECTOR_ID) REFERENCES $TABLE_DIRECTORS($ID)
						);"""
        )
        db.execSQL(
            """create table $TABLE_ACTORS (
						$ID integer primary key autoincrement, 
						$ACTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_MOVIE_ACTOR (
						$ID integer primary key autoincrement, 
						$MOVIE_ID numeric, 
						$ACTOR_ID numeric,
						FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIES($ID), 
						FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTORS($ID)
						);"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTORS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTORS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIES")
        onCreate(db)
    }

    /**
     *  Insert in the *TABLE_MOVIE_ACTOR*, essentially linking an movie and a actor
     */
    private fun linkMovieAndActor(database: SQLiteDatabase, movieId: Long, actorId: Long) {
        val values = ContentValues()
        values.put(MOVIE_ID, movieId)
        values.put(ACTOR_ID, actorId)
        database.insert(TABLE_MOVIE_ACTOR, null, values)
    }

    /**
     *  Inserts the movie, the director, list of actors of the movie and then the connection between them.
     */
    fun insert(title: String, director: String, actors: List<String>) {
        writableDatabase.use { database ->
            val newDirector = ContentValues()
            newDirector.put(DIRECTOR_NAME, director)
            val directorId = insertValue(database, TABLE_DIRECTORS, newDirector)

            val newMovie = ContentValues()
            newMovie.put(MOVIE_TITLE, title)
            newMovie.put(MOVIE_DIRECTOR_ID, directorId)
            val movieId = insertValue(database, TABLE_MOVIES, newMovie)

            actors.forEach {
                val newActor = ContentValues()
                newActor.put(ACTOR_NAME, it.trim())
                val actorId = insertValue(database, TABLE_ACTORS, newActor)
                linkMovieAndActor(database, movieId, actorId)
            }
        }
    }

    /**
     *  Drop all information is the database
     */
    fun clear() {
        writableDatabase.use { onUpgrade(it, 0, 0) }
    }

    /**
     * Insert the value in given table and field, then return its ID
     */
    private fun insertValue(
        database: SQLiteDatabase, table: String, values: ContentValues
    ): Long {
        return database.insert(table, null, values)
    }

    /**
     * Perform a simple query
     *
     * Not the query() function has almost all parameters as *null*, you should check up on these.
     * maybe you don't even need the performRawQuery() function?
     */
    fun performQuery(table: String, columns: Array<String>, selection: String? = null):
            ArrayList<String> {
        assert(columns.isNotEmpty())
        readableDatabase.use { database ->
            query(database, table, columns, selection).use { cursor ->
                return readFromCursor(cursor, columns.size)
            }
        }
    }

    /**
     * Run a raw query, the parameters are for easier debugging and reusable code
     */
    fun performRawQuery(
        select: Array<String>, from: Array<String>, join: Array<String>, where: String? = null
    ): ArrayList<String> {

        val query = StringBuilder("SELECT ")
        for ((i, field) in select.withIndex()) {
            query.append(field)
            if (i != select.lastIndex) query.append(", ")
        }

        query.append(" FROM ")
        for ((i, table) in from.withIndex()) {
            query.append(table)
            if (i != from.lastIndex) query.append(", ")
        }

        query.append(" WHERE ")
        for ((i, link) in join.withIndex()) {
            query.append(link)
            if (i != join.lastIndex) query.append(" and ")
        }

        if (where != null) query.append(" and $where")

        readableDatabase.use { db ->
            db.rawQuery("$query;", null).use { cursor ->
                return readFromCursor(cursor, select.size)
            }
        }
    }

    /**
     * Read the contents from the cursor and return it as an arraylist
     */
    private fun readFromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
        val result = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = StringBuilder("")
            for (i in 0 until numberOfColumns) {
                item.append("${cursor.getString(i)} ")
            }
            result.add("$item")
            cursor.moveToNext()
        }
        return result
    }

    /**
     * Use a query with default arguments
     */
    private fun query(
        database: SQLiteDatabase, table: String, columns: Array<String>, selection: String?
    ): Cursor {
        return database.query(table, columns, selection, null, null, null, null, null)
    }
}