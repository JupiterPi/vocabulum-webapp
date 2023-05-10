package jupiterpi.vocabulum.webappserver.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import jupiterpi.vocabulum.webappserver.db.models.Users

object Auth {
    fun verifyToken(token: String): FirebaseToken? {
        return try {
            FirebaseAuth.getInstance().verifyIdToken(token)
        } catch (e: FirebaseAuthException) { null }
    }

    fun verifyAuthHeader(authHeader: String) = verifyToken(authHeader.split(" ")[1])

    fun getToken(authHeader: String) = verifyAuthHeader(authHeader) ?: throw Exception("Invalid auth token!")

    fun getUser(authHeader: String) = Users.findById(getToken(authHeader).uid)

    fun makeAdmin(uid: String, makeAdmin: Boolean) {
        FirebaseAuth.getInstance().setCustomUserClaims(uid, mapOf("admin" to makeAdmin))
    }
}

fun FirebaseToken.isAdmin() = claims["admin"] == true
fun FirebaseToken.assertAdmin() {
    if (!isAdmin()) throw Exception("Admin-only!")
}