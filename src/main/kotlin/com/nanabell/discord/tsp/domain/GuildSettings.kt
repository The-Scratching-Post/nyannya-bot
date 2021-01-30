package com.nanabell.discord.tsp.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "guild_settings")
data class GuildSettings(

    @Id
    @Column(name = "id")
    var id: Long,

    @Column(name = "info_channel")
    var infoChannel: Long? = null,

    @Column(name = "join_message")
    var joinMsg: String? = null,

    @Column(name = "leave_message")
    var leaveMsg: String? = null

)
