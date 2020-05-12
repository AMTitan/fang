package de.bigboot.ggtools.fang.utils

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Member
import kotlinx.coroutines.reactive.awaitFirstOrNull

suspend fun Guild.findMember(name: String): Member? {
    return members
        .filter {
            it.username.toLowerCase() == name || it.displayName.toLowerCase() == name
        }
        .awaitFirstOrNull()
}