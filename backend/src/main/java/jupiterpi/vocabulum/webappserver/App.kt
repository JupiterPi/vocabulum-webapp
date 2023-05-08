package jupiterpi.vocabulum.webappserver

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.db.lectures.Lectures
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import jupiterpi.vocabulum.webappserver.db.Storage
import org.bson.Document
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

fun main(args: Array<String>) {
    println("++++++++++ Vocabulum App (Server) ++++++++++")
    println("With CoreService: $CoreService")
    runApplication<App>(*args)
}

@SpringBootApplication
class App

val datastore: Datastore = DatastoreOptions.getDefaultInstance().service

@RestController
class Controller {
    @GetMapping("/")
    fun root() = "Vocabulum Webapp server working!\n<br>\n${Date()}"
}

object CoreService {
    private val exampleLines: Map<String, List<Lectures.ExampleLine>> by lazy {
        Database.get().lectures.allExampleLines
    }
    fun getExampleLines(vocabulary: Vocabulary)
            = exampleLines.entries.firstOrNull { it.key == vocabulary.baseForm }?.value ?: listOf()

    private const val BUCKET = "vocabulum-dictionary"
    init {
        val portions = mutableMapOf<String, List<List<String>>>()
        val portionsDocument = Storage.readJsonStorageFile(BUCKET, "portions.json")
        portionsDocument.getList("portions", Document::class.java).forEach {
            val name = it.getString("name")
            val file = Storage.readStorageFile(BUCKET, "portions/${it.getString("file")}").file
            portions[name] = file.split("\n\n").map { it.lines() }
        }

        val lectures = mutableMapOf<String, List<String>>()
        val lecturesDocument = Storage.readJsonStorageFile(BUCKET, "lectures.json")
        lecturesDocument.getList("lectures", Document::class.java).forEach {
            val name = it.getString("name")
            val file = Storage.readStorageFile(BUCKET, "lectures/${it.getString("file")}")
            lectures[name] = file.lines
        }

        Database.get().load(portions, lectures)
    }
}