package com.nanabell.discord.tsp.commands

import com.jagrosh.jdautilities.command.CommandEvent
import com.jagrosh.jdautilities.command.annotation.JDACommand
import com.nanabell.discord.tsp.repository.GuildSettingsRepository
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import javax.inject.Singleton

@Singleton
@JDACommand.Module("test")
class CommonModule : JDAModule {


    @JDACommand(name = ["test"], ownerCommand = true)
    fun test(event: CommandEvent) {
        event.message.delete().queue()

        event.jda.eventManager.handle(GuildMemberJoinEvent(event.jda,  event.responseNumber, event.member))
        event.jda.eventManager.handle(GuildMemberRemoveEvent(event.jda,  event.responseNumber, event.guild, event.member.user, event.member))
    }

}