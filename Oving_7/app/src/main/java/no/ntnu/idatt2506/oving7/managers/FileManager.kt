package no.ntnu.idatt2506.oving7.managers

import androidx.appcompat.app.AppCompatActivity
import java.io.*

class FileManager(private val activity: AppCompatActivity) {
    private val filename: String = "movies_details.txt"
    private var dir: File = activity.filesDir
    private var file: File = File(dir, filename)

    //Device File Mangager -> Data/Data/no.ntnu.idatt2506.oving7/files/movies_details_new.txt
    private val filenameWriteTo: String = "movies_details_new.txt"
    private var dirWriteTo: File? = activity.filesDir
    private var fileWriteTo: File = File(dirWriteTo, filenameWriteTo)

    /**
     * write to a movies_details_new.txt
     */
    fun write(str: String) {
        PrintWriter(fileWriteTo).use { writer ->
            writer.println(str)
        }
    }

    /**
     * reads movies_details.txt file from res/raw
     */
    fun readFileFromResFolder(fileId: Int): String {
        val content = StringBuffer("")
        try {
            val inputStream: InputStream = activity.resources.openRawResource(fileId)
            val reader = BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    content.append(line)
                    content.append("\n")
                    line = reader.readLine()
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content.toString()
    }
}