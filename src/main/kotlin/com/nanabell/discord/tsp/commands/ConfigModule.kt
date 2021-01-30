package com.nanabell.discord.tsp.commands

import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.command.annotation.JDACommand
import com.nanabell.discord.tsp.repository.GuildSettingsRepository
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import java.lang.NumberFormatException
import javax.inject.Singleton

@Singleton
@JDACommand.Module("setCommand")
class ConfigModule(private val repository: GuildSettingsRepository) : JDAModule {

    @JDACommand(
        name = ["set"],
        children = ["setInfoChannel", "setJoinMessage", "setLeaveMessage"],
        category = JDACommand.Category(name = "CATEGORY", location = ConfigModule::class),
        userPermissions = [Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL],
        help = "Set Config for this Server."
    )
    fun setCommand(event: CommandEvent) {
        event.replyWarning("Please use the SubCommands `channel`, `join` or `leave`")
    }

    @JDACommand(
        name = ["channel"],
        arguments = "<channel>",
        guildOnly = true,
        userPermissions = [Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL],
        help = "Set the Info Channel for this Guild"
    )
    fun setInfoChannel(event: CommandEvent) {
        val channel = parseChannel(event.message, event.args.trim().ifEmpty { null })
        if (channel == null) {
            event.replyError("Unable to find any channel with the provided input!")
            return
        }

        repository.ensureCreated(event.guild.idLong).setInfoChannel(event.guild.idLong, channel.idLong)
        event.replySuccess("Info Channel has been set to: ${channel.asMention}")
    }

    @JDACommand(
        name = ["join"],
        arguments = "<join message>",
        guildOnly = true,
        userPermissions = [Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL],
        help = "Set the Join Message for this Guild, will be sent in Info Channel"
    )
    fun setJoinMessage(event: CommandEvent) {
        val args = event.args.trim().ifEmpty { null }
        repository.ensureCreated(event.guild.idLong).setJoinMessage(event.guild.idLong, args)

        if (args == null) {
            event.replySuccess("Join Message has been Disabled.")
        } else {
            event.replySuccess("Join Message has been set to $args")
        }

    }

    @JDACommand(
        name = ["leave"],
        arguments = "<leave message>",
        guildOnly = true,
        userPermissions = [Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL],
        help = "Set the Leave Message for this Guild, will be sent in Info Channel"
    )
    fun setLeaveMessage(event: CommandEvent) {
        val args = event.args.trim().ifEmpty { null }
        repository.ensureCreated(event.guild.idLong).setLeaveMessage(event.guild.idLong, args)

        if (args == null) {
            event.replySuccess("Leave Message has been Disabled.")
        } else {
            event.replySuccess("Leave Message has been set to $args")
        }
    }

    private fun parseChannel(message: Message, args: String?): TextChannel? {
        val guild = message.guild

        // If we have a Mention, great use the first one we can talk in
        val channel = message.mentionedChannels.firstOrNull { it.type == ChannelType.TEXT && it.canTalk() }
        if (channel != null) return channel

        // If args are null return null
        if (args == null) return null
        val parts = args.split(" ")

        // Try to find a Channel by ID
        for (part in parts) {
            try {
                val channel = guild.getTextChannelById(part)
                if (channel != null && channel.canTalk())
                    return channel
            } catch (e: NumberFormatException) {
                // Ignored
            }

        }

        // Lastly try to find one by name
        for (part in parts) {
            val channel = guild.getTextChannelsByName(part, true).firstOrNull()
            if (channel != null && channel.canTalk())
                return channel
        }

        // Give up
        return null
    }

    companion object {
        private const val CATEGORY: String = "config"
    }
}