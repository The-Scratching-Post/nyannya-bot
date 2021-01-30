package com.nanabell.discord.tsp.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("discord")
class DiscordConfiguration {

    var token: String? = null

    @ConfigurationProperties("command")
    class CommandConfiguration {

        var prefix: String = "--"

        var ownerId: String? = null
    }
}
