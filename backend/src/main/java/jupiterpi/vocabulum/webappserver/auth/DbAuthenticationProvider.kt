package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.core.db.Database
import jupiterpi.vocabulum.core.users.User
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.security.Principal

class DbAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val email = authentication.name
        val password = authentication.credentials.toString()

        if (Database.get().users.all.filter { user: User -> user.email == email && user.password == password }.size != 1) {
            throw BadCredentialsException("Username or password wrong, or unknown user")
        } else {
            return UsernamePasswordAuthenticationToken(email, password, listOf(SimpleGrantedAuthority(USER_ROLE)))
        }
    }

    override fun supports(aClass: Class<*>): Boolean {
        return aClass == UsernamePasswordAuthenticationToken::class.java
    }

    companion object {
        private const val USER_ROLE = "VK_USER"

        fun getUser(principal: Principal): User = try {
            Database.get().users.all.first { it.email == principal.name }
        } catch (e: NoSuchElementException) {
            throw Exception("could not find user from principal: ${principal.name}")
        }
    }
}