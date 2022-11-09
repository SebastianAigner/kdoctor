package org.jetbrains.kotlin.doctor.entity

import co.touchlab.kermit.Logger
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.kotlin.doctor.KDOCTOR_VERSION

@Serializable
enum class EnvironmentPiece {
    @SerialName("kdoctor") Kdoctor,
    @SerialName("macos") Macos,
    @SerialName("jdk") Jdk,
    @SerialName("androidStudio") AndroidStudio,
    @SerialName("kotlinPlugin") KotlinPlugin,
    @SerialName("cocoapods") Cocoapods,
    @SerialName("kmmPlugin") KmmPlugin,
    @SerialName("ruby") Ruby,
    @SerialName("rubyGems") RubyGems,
    @SerialName("xcode") Xcode;
}

@Serializable
enum class CompatibilityPriority {
    @SerialName("error") Error,
    @SerialName("warning") Warning,
    @SerialName("info") Info
}

@Serializable
data class CompatibilityRange(
    @SerialName("from") val from: String? = null,
    @SerialName("fixedIn") val fixedIn: String? = null
)

@Serializable
data class CompatibilityProblem(
    @SerialName("url") val url: String,
    @SerialName("text") val text: String,
    @SerialName("priority") val priority: CompatibilityPriority,
    @SerialName("matrix") val matrix: Map<EnvironmentPiece, CompatibilityRange>
)

@Serializable
data class Compatibility(
    @SerialName("latestKdoctor") val latestKdoctor: String,
    @SerialName("problems") val problems: List<CompatibilityProblem>
) {
    companion object {
        suspend fun download() = try {
            Logger.d("Compatibility.download")
            //todo fix json url!
            val compatibilityJson = System.httpClient.get(
                "https://github.com/Kotlin/kdoctor/raw/wip/kdoctor/src/commonMain/resources/compatibility.json"
            ).bodyAsText()
            Logger.d(compatibilityJson)
            Json.decodeFromString<Compatibility>(compatibilityJson)
        } catch (e: Exception) {
            Logger.e("Compatibility.download error ${e.message}", e)
            Compatibility(KDOCTOR_VERSION, emptyList())
        }
    }
}