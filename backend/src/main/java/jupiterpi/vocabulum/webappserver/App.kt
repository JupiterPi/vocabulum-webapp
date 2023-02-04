package jupiterpi.vocabulum.webappserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
    println("++++++++++ Vocabulum App (Server) ++++++++++")
    println("With CoreService, i18n=${CoreService.i18n}")
    runApplication<App>(*args)
}