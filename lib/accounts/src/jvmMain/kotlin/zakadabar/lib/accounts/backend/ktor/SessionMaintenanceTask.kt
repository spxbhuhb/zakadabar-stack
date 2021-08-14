/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend.ktor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import zakadabar.lib.accounts.data.ModuleSettings
import zakadabar.core.server.Server
import zakadabar.core.setting.setting

object SessionMaintenanceTask {

    private val settings by setting<ModuleSettings>()

    val updateChannel = Channel<SessionCacheEntry>()

    private val logger = LoggerFactory.getLogger("sessions") !!

    private val mutex = Mutex()

    private var updateRunning = false
    private var expireRunning = false
    private var trackerRunning = false

    fun start() {
        GlobalScope.launch { updateJob() }
        GlobalScope.launch { expireJob() }
        GlobalScope.launch { trackerMaintenanceJob() }
    }

    private suspend fun updateJob() {
        mutex.withLock {
            if (updateRunning) return@updateJob
            updateRunning = true
        }

        if (updateRunning)
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
        mutex.withLock {
            if (expireRunning) return@expireJob
            expireRunning = true
        }

        while (! Server.shutdown) {
            try {
                val cutoff = Clock.System.now().minus(settings.sessionTimeout, DateTimeUnit.MINUTE)
                val toDelete = transaction {
                    SessionTable
                        .select { SessionTable.lastAccess less cutoff.toJavaInstant() }
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

    private suspend fun trackerMaintenanceJob() {
        mutex.withLock {
            if (trackerRunning) return@trackerMaintenanceJob
            trackerRunning = true
        }

        while (! Server.shutdown) {
            try {
                val cutoff = Clock.System.now().minus(settings.expirationCheckInterval, DateTimeUnit.SECOND)

                expiredMapMutex.withLock {
                    expiredToNew.filter { it.value.createdAt < cutoff }.forEach {
                        expiredToNew.remove(it.key)
                    }
                }

            } catch (ex: Exception) {
                logger.error("session tracker maintenance error", ex)
            }
            delay(settings.expirationCheckInterval * 1000)
        }
    }

}