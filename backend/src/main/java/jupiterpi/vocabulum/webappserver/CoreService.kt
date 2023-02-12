package jupiterpi.vocabulum.webappserver

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import jupiterpi.tools.files.TextFile
import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.db.lectures.Lectures.ExampleLine
import jupiterpi.vocabulum.core.vocabularies.Vocabulary
import org.slf4j.LoggerFactory

object CoreService {
    private val exampleLines: Map<String, List<ExampleLine>> by lazy {
        Database.get().lectures.allExampleLines
    }
    fun getExampleLines(vocabulary: Vocabulary)
    = exampleLines.entries.firstOrNull { it.key == vocabulary.baseForm }?.value ?: listOf()

    init {
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.getLogger("org.mongodb.driver").level = Level.ERROR
        // as per https://stackoverflow.com/a/40884256/13164753

        val connectUrl = TextFile("mongodb_connect_url-vocabulum_data.txt").getLine(0)
        println("CoreService: connectUrl = '$connectUrl'")
        Database.get().connectAndLoad(connectUrl)
        Database.get().prepareWordbase()
    }
}