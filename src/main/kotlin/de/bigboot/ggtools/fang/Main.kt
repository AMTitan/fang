package de.bigboot.ggtools.fang

import de.bigboot.ggtools.fang.di.commandsModule
import de.bigboot.ggtools.fang.di.databaseModule
import de.bigboot.ggtools.fang.di.serviceModule
import discord4j.core.DiscordClient
import kotlinx.coroutines.reactive.awaitSingle
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.Configuration
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Level.*
import org.koin.core.logger.MESSAGE
import org.tinylog.kotlin.Logger

suspend fun main() {
    org.tinylog.configuration.Configuration.set("level", Config.bot.log_level)

    Flyway
        .configure()
        .locations("classpath:${Fang::class.java.packageName.replace(".", "/")}")
        .dataSource(Config.database.url, Config.database.user, Config.database.pass)
        .baselineOnMigrate(true)
        .load()
        .migrate()

    startKoin {
        logger(object : org.koin.core.logger.Logger() {
            override fun log(level: Level, msg: MESSAGE) {
                when (level) {
                    ERROR -> Logger.error(msg)
                    INFO -> Logger.info(msg)
                    DEBUG -> Logger.debug(msg)
                    NONE -> Logger.trace(msg)
                }
            }
        })

        modules(
            databaseModule,
            serviceModule,
            commandsModule
        )
    }

    val client = DiscordClient.create(Config.bot.token).login().awaitSingle()

    Fang(client).run()
}
