package com.nanabell.discord.tsp.listener

import com.nanabell.discord.tsp.repository.GuildSettingsRepository
import io.micronaut.scheduling.annotation.Scheduled
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class JoinLeaveListener(private val jda: JDA, private val repository: GuildSettingsRepository) : EventListener {

    private val logger = LoggerFactory.getLogger(JoinLeaveListener::class.java)

    @Scheduled(initialDelay = "1s")
    fun init() {
        jda.addEventListener(this)
        logger.info("Started ${this.javaClass.simpleName}")
    }

    override fun onEvent(event: GenericEvent) {
        when (event) {
            is GuildMemberJoinEvent -> onMemberJoinOrLeave(event.guild, event.user, true)
            is GuildMemberRemoveEvent -> onMemberJoinOrLeave(event.guild, event.user, false)
        }
    }

    private fun onMemberJoinOrLeave(guild: Guild, user: User, isWelcome: Boolean) {
        logger.info("$user ${if (isWelcome) "joined" else "left"} $guild")
        val settings = repository.getOrCreate(guild.idLong)

        val notifyChannelId = settings.infoChannel ?: return
        val notifyChannel = guild.getTextChannelById(notifyChannelId) ?: return

        if (!notifyChannel.canTalk()) return
        val msg = (if (isWelcome) settings.joinMsg else settings.leaveMsg) ?: return
        val formatted = msg
            .replace("%user_mention%", user.asMention)
            .replace("%user%", user.asTag)
            .replace("%guild%", guild.name)

        notifyChannel.sendMessage(formatted).queue()
    }

}