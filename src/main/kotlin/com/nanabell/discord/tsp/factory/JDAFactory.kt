package com.nanabell.discord.tsp.factory

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandClient
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.nanabell.discord.tsp.commands.JDAModule
import com.nanabell.discord.tsp.config.DiscordConfiguration
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Parallel
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Factory
open class JDAFactory {

    private val logger = LoggerFactory.getLogger(JDAFactory::class.java)

    @Parallel
    @Singleton
    open fun buildJda(configuration: DiscordConfiguration, client: CommandClient): JDA {
        logger.info("Building JDA")
        val builder = JDABuilder.create(
            configuration.token,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_EMOJIS
        )

        builder.setToken(configuration.token)
        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
        builder.addEventListeners(client)

        return builder.build().also {
            logger.info("Constructed JDAClient with intents: ${it.gatewayIntents.joinToString()}")
        }
    }

    @Singleton
    open fun buildCommand(
        configuration: DiscordConfiguration.CommandConfiguration,
        context: ApplicationContext
    ): CommandClient {
        logger.info("Building CommandClient")
        val builder = CommandClientBuilder()

        builder.setPrefix(configuration.prefix)
        logger.info("CommandClient Prefix is set to '${configuration.prefix}'")

        logger.info("CommandClient Loading Commands")
        val modules = context.getBeansOfType(JDAModule::class.java)
        modules.forEach { builder.addAnnotatedModule(it) }

        builder.setOwnerId(configuration.ownerId)
        logger.info("CommandClient OwnerId = ${configuration.ownerId}")

        return builder.build().also {
            logger.info("Constructed CommandClient with ${getCommandSize(it)} command/s")
        }
    }

    private fun getCommandSize(client: CommandClient): Int {
        var size = client.commands.size
        size += client.commands.map { getChildCommandSize(it) }.sum()

        return size
    }

    private fun getChildCommandSize(command: Command): Int {
        var size = command.children.size
        size += command.children.map { getChildCommandSize(it) }.sum()

        return size
    }
}