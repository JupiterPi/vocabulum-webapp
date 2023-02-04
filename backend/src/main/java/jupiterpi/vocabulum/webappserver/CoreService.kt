package jupiterpi.vocabulum.webappserver

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import jupiterpi.tools.files.TextFile
import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.i18n.I18n

import org.slf4j.LoggerFactory

object CoreService {
    val i18n: I18n

    init {
        val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
        loggerContext.getLogger("org.mongodb.driver").level = Level.ERROR
        // as per https://stackoverflow.com/a/40884256/13164753

        val connectUrl = TextFile("mongodb_connect_url-vocabulum_data.txt").getLine(0)
        println("CoreService: connectUrl = '$connectUrl'")
        Database.get().connectAndLoad(connectUrl)
        Database.get().prepareWordbase()
        i18n = Database.get().i18ns.de()
    }
}