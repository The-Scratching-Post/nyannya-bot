package com.nanabell.discord.tsp.repository

import com.nanabell.discord.tsp.domain.GuildSettings
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
abstract class GuildSettingsRepository : JpaRepository<GuildSettings, Long> {

    fun getOrCreate(id: Long): GuildSettings = findById(id).orElseGet { save(GuildSettings(id)) }

    fun ensureCreated(id: Long): GuildSettingsRepository { getOrCreate(id); return this }

    @Query("UPDATE GuildSettings SET infoChannel = :infoChannel WHERE id = :id")
    abstract fun setInfoChannel(id: Long, infoChannel: Long): GuildSettings

    @Query("UPDATE GuildSettings SET joinMsg = :joinMessage WHERE id = :id")
    abstract fun setJoinMessage(id: Long, joinMessage: String?)

    @Query("UPDATE GuildSettings SET leaveMsg = :leaveMessage WHERE id = :id")
    abstract fun setLeaveMessage(id: Long, leaveMessage: String?)

}