package net.unaussprechlich.warlordsplus.stats

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI

data class WarlordsSR(val success: Boolean, val data: String)

@KtorExperimentalAPI
object Stats {


    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getData(): WarlordsSR {
        return client.get<WarlordsSR>("https://warlordssr.unaussprechlich.net/api/uuid/01c4a20e0a60466fbd98ea71c346e5e4")
    }
}