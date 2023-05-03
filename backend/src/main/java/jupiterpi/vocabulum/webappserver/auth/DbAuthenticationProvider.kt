package jupiterpi.vocabulum.webappserver.auth

import jupiterpi.vocabulum.webappserver.db.models.User
import jupiterpi.vocabulum.webappserver.db.models.Users
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

        if (Users.findByCredentials(email, password) == null) {
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

        fun getUser(principal: Principal): User
        = Users.findByEmail(principal.name)
            ?: throw Exception("Could not find user from principal: ${principal.name}")
    }
}