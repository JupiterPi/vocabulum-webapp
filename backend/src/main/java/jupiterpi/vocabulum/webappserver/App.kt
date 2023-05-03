package jupiterpi.vocabulum.webappserver

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.DatastoreOptions
import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.db.lectures.Lectures
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    println("++++++++++ Vocabulum App (Server) ++++++++++")
    println("With CoreService: $CoreService")
    runApplication<App>(*args)
}

@SpringBootApplication
class App

val datastore: Datastore = DatastoreOptions.getDefaultInstance().service

object CoreService {
    private val exampleLines: Map<String, List<Lectures.ExampleLine>> by lazy {
        Database.get().lectures.allExampleLines
    }
    fun getExampleLines(vocabulary: Vocabulary)
            = exampleLines.entries.firstOrNull { it.key == vocabulary.baseForm }?.value ?: listOf()

    init {
        Database.get().load()
    }
}