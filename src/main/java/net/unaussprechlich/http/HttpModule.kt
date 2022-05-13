package net.unaussprechlich.http

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import net.unaussprechlich.warlordsplus.module.modules.stats.WarlordsSrApiResponse
import net.unaussprechlich.http.serializable.Release

object HttpModule {

    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    useArrayPolymorphism = false
                    encodeDefaults = false
                    //allowStructuredMapKeys = true
                    coerceInputValues = true
                }
            )
        }
    }

    object WarlordsSrApi{
        suspend fun getWarlordsSrResponse (name : String) : WarlordsSrApiResponse {
            return client.get("https://warlordssr.unaussprechlich.net/api/$name")
        }
    }

    object GitHubApi{
        suspend fun getReleases(owner : String, repository : String) : List<Release>{
            return client.get<List<Release>>("https://api.github.com/repos/$owner/$repository/releases?per_page=100")
        }
    }


}