@file:Suppress("ClassName", "ClassNaming", "unused", "LongMethod")

package de.bigboot.ggtools.fang.db.migrations

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context

class V11__add_match_making : BaseJavaMigration() {
    override fun migrate(context: Context) {
        context.connection.prepareStatement("""
        |alter table Users
        |   drop column skill;
        """.trimMargin()).execute()

        context.connection.prepareStatement("""
        |alter table Users
        |   add rating binary(16);
        """.trimMargin()).execute()

        context.connection.prepareStatement("""
        |alter table Users
        |    add constraint fk_Users_userRating_id
        |        foreign key (rating) references UsersRating (id);
        """.trimMargin()).execute()

        context.connection.prepareStatement("""
        |create table if not exists UsersRating
        |(
        |    id              binary(16) not null
        |        primary key,
        |    rating          double     not null,
        |    ratingDeviation double     not null,
        |    volatility      double     not null
        |);
        """.trimMargin()).execute()
    }
}
