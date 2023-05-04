package jupiterpi.vocabulum.webappserver.db

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName

object Secrets {
    private val client = SecretManagerServiceClient.create()
    private fun getStringSecret(name: String, version: String = "1"): String {
        val secretName = SecretVersionName.newBuilder()
            .setProject("vocabulum-de")
            .setSecret(name)
            .setSecretVersion(version)
            .build()
        val secret = client.accessSecretVersion(secretName).also { println(it) }
        return secret.payload.data.toStringUtf8()
    }

    val openaiApiKey = getStringSecret("openai-api-key")
}