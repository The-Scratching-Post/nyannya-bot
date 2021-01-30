package com.nanabell.discord.tsp.listener

import com.nanabell.discord.tsp.domain.GuildSettings
import com.nanabell.discord.tsp.repository.GuildSettingsRepository
import io.micronaut.scheduling.annotation.Scheduled
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class GuildJoinListener(
    private val jda: JDA,
    private val repository: GuildSettingsRepository
) : EventListener {

    private val logger = LoggerFactory.getLogger(GuildJoinListener::class.java)

    @Scheduled(initialDelay = "1s")
    fun init() {
        jda.addEventListener(this)
        logger.info("Started ${this::class.java.simpleName}")
    }

    override fun onEvent(event: GenericEvent) {
        if (event !is GuildJoinEvent) return

        logger.info("Joined Guild ${event.guild}")
        if (!repository.existsById(event.guild.idLong)) {
            logger.info("No GuildConfig exists for ${event.guild}... Creating default")
            repository.save(GuildSettings(event.guild.idLong))
        }
    }

}