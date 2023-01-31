package jupiterpi.vocabulum.webappserver

import jupiterpi.tools.files.TextFile
import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.i18n.I18n

object CoreService {
    val i18n: I18n

    init {
        val connectUrl = TextFile("mongodb_connect_url-vocabulum_data.txt").getLine(0)
        println("CoreService: connectUrl = '$connectUrl'")
        Database.get().connectAndLoad(connectUrl)
        Database.get().prepareWordbase()
        i18n = Database.get().i18ns.de()
    }
}