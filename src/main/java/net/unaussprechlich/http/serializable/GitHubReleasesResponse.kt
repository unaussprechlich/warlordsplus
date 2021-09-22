package net.unaussprechlich.http.serializable

import kotlinx.serialization.Serializable

@Serializable
data class Author (

    val gists_url : String,
    val repos_url : String,
    val following_url : String,
    val starred_url : String,
    val login : String,
    val followers_url : String,
    val type : String,
    val url : String,
    val subscriptions_url : String,
    val received_events_url : String,
    val avatar_url : String,
    val events_url : String,
    val html_url : String,
    val site_admin : Boolean,
    val id : Int,
    val gravatar_id : String,
    val node_id : String,
    val organizations_url : String

)

@Serializable
data class Uploader (

    val gists_url : String,
    val repos_url : String,
    val following_url : String,
    val starred_url : String,
    val login : String,
    val followers_url : String,
    val type : String,
    val url : String,
    val subscriptions_url : String,
    val received_events_url : String,
    val avatar_url : String,
    val events_url : String,
    val html_url : String,
    val site_admin : Boolean,
    val id : Int,
    val gravatar_id : String,
    val node_id : String,
    val organizations_url : String

)

@Serializable
data class Assets (

    val created_at : String,
    val browser_download_url : String,
    val label : String?,
    val url : String,
    val download_count : Int,
    val content_type : String,
    val size : Int,
    val updated_at : String,
    val uploader : Uploader,
    val name : String,
    val id : Int,
    val state : String,
    val node_id : String

)

@Serializable
data class Release (

    val author : Author,
    val tag_name : String,
    val created_at : String,
    val body : String?,
    val url : String,
    val assets_url : String,
    val assets: List<Assets>,
    val prerelease : Boolean,
    val html_url : String,
    val target_commitish : String,
    val draft : Boolean,
    val zipball_url : String,
    val name : String,
    val upload_url : String,
    val id : Int,
    val published_at : String,
    val tarball_url : String,
    val node_id : String

)