package jupiterpi.vocabulum.webappserver.db

import com.google.cloud.storage.StorageOptions
import jupiterpi.vocabulum.core.util.TextFile
import org.bson.Document
import java.nio.charset.StandardCharsets

object Storage {
    private val storage = StorageOptions
        .newBuilder()
        .setProjectId("vocabulum-de")
        .build().service

    fun readStorageFile(bucketName: String, objectName: String): TextFile {
        val content = storage.readAllBytes(bucketName, objectName)
        return TextFile(String(content, StandardCharsets.UTF_8).lines())
    }

    fun readJsonStorageFile(bucketName: String, objectName: String): Document {
        val content = readStorageFile(bucketName, objectName).file
        return Document.parse(content)
    }
}