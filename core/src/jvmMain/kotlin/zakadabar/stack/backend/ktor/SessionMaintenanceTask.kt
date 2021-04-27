/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.session.SessionTable
import zakadabar.stack.backend.util.Setting
import zakadabar.stack.backend.util.default
import zakadabar.stack.backend.util.setting
import zakadabar.stack.data.builtin.settings.SessionBackendSettingsDto

object SessionMaintenanceTask {

    private val settings by setting<SessionBackendSettingsDto>("zakadabar.stack.session")

    val updateChannel = Channel<SessionCacheEntry>()

    private val logger = LoggerFactory.getLogger("sessions") !!

    fun start() {
        // TODO prevent start again
        GlobalScope.launch { updateJob() }
        GlobalScope.launch { expireJob() }
    }

    private suspend fun updateJob() {
        while (! Server.shutdown) {
            val entry = updateChannel.receive()
            try {
                transaction {
                    SessionTable.update({ SessionTable.id eq entry.sessionId }) {
                        it[lastAccess] = entry.createdAt.toJavaInstant()
                    }
                }
            } catch (ex: Exception) {
                logger.error("session update error", ex)
                delay(10000) // TODO a slightly better error recovery mechanism
            }
        }
    }

    private suspend fun expireJob() {
        while (! Server.shutdown) {
            try {
                val cutoff = Clock.System.now().toJavaInstant().minusMillis(settings.sessionTimeout * 1000)
                val toDelete = transaction {
                    SessionTable
                        .select { SessionTable.lastAccess less cutoff }
                        .map { it[SessionTable.id] }
                }
                toDelete.forEach {
                    SessionStorageSql.invalidate(it)
                }
            } catch (ex: Exception) {
                logger.error("session expire error", ex)
            }
            delay(settings.expirationCheckInterval * 1000)
        }
    }

}