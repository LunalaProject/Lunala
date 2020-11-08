package com.gabriel.lunala.project.util

import kotlinx.serialization.Serializable

@Serializable
data class LunalaDiscordConfig(
    val general: GeneralConfigSection,
    val client: ClientConfigSection,
    val database: DatabaseConfigSection,
    val discord: DiscordConfigSection,
    val website: WebsiteConfigSection
) {

    @Serializable
    data class GeneralConfigSection(
        val environment: String
    )

    @Serializable
    data class ClientConfigSection(
        val token: String,
        val shards: Int
    )

    @Serializable
    data class DatabaseConfigSection(
        val host: String,
        val port: String,
        val database: String,
        val user: String,
        val password: String
    )

    @Serializable
    data class DiscordConfigSection(
        val discord: String,
        val discordId: String
    )

    @Serializable
    data class WebsiteConfigSection(
        val url: String,
        val key: String
    )
}

class ConfigInspectionException(message: String?, throwable: Throwable? = null): Exception(message, throwable)