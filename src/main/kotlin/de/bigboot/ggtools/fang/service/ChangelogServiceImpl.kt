package de.bigboot.ggtools.fang.service

import de.bigboot.ggtools.fang.db.Version
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.min

private val CHANGELOG = listOf(
    listOf(
        "Fixed queue timers",
    ),
    listOf(
        "Fixed queue timers when match takes longer than 90 minutes",
    ),
    listOf(
        "Increase msg update times to fix queue emoji problems",
    ),
    listOf(
        "Fix queue channel cleanup",
    ),
    listOf(
        "Added Frankenbuild queue",
    ),
    listOf(
        "Added support for multiple queues",
        "Update dependencies",
    ),
    listOf(
        "Fixed a bug causing player to be unable to join a queue after using the dropout feature"
    ),
)

class ChangelogServiceImpl: KoinComponent, ChangelogService {
    private val database: Database by inject()

    override val changelog: Changelog = transaction(database) {
        val version = Version.all().firstOrNull() ?: Version.new {
            this.revision = 0
        }

        val latestRevision = CHANGELOG.size
        val lastRevision = min(version.revision, latestRevision)

        version.revision = latestRevision

        CHANGELOG.subList(lastRevision, latestRevision).flatten()
    }
}
