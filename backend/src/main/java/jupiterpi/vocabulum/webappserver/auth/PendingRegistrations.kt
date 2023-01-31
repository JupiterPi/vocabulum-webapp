package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.users.User
import java.util.*

class PendingRegistrations {
    val registrations: MutableMap<String, Registration> = mutableMapOf()

    class Registration(
        val dto: RegistrationDTO,
        val expiration: Date,
    ) {
        val isExpired: Boolean
            get() = expiration.time <= Date().time
    }

    private fun cleanupRegistrations() = registrations.filter { (_, registration) -> registration.isExpired }.forEach { registrations.remove(it.key) }

    fun addPendingRegistration(registration: RegistrationDTO): String {
        cleanupRegistrations()
        val id = UUID.randomUUID().toString()
        registrations[id] = Registration(registration, Calendar.getInstance().apply { add(Calendar.MINUTE, 5) }.time)
        return id
    }

    fun confirmRegistration(id: String): User? {
        cleanupRegistrations()
        val registration = registrations[id] ?: return null
        registrations.remove(id)
        val user = registration.dto.let {
            User.createUser(it.username, it.email, it.password)
        }
        Database.get().users.addUser(user)
        Database.get().users.save()
        return user
    }
}