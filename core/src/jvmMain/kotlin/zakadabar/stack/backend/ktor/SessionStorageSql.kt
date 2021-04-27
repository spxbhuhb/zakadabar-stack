/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.sessions.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import zakadabar.stack.backend.data.builtin.session.SessionTable
import zakadabar.stack.backend.util.Setting
import zakadabar.stack.backend.util.default
import zakadabar.stack.backend.util.setting
import zakadabar.stack.data.builtin.settings.SessionBackendSettingsDto
import java.io.ByteArrayOutputStream
import kotlin.coroutines.coroutineContext

/**
 * A Ktor session storage with SQL backend. It saves/loads [StackSession] objects
 * into/from [SessionTable].
 *
 * This implementation is really far from good but I got feed up with Ktor session
 * handling and went for a "just works" solution.
 */
object SessionStorageSql : SessionStorage {

    private val settings by setting<SessionBackendSettingsDto>("zakadabar.stack.session")

    /**
     * Cache access is synchronised with this mutex. We use cache for simple lookup
     * and insert, that should be fast enough so locking the whole cache is not a
     * problem.
     */
    private val mutex = Mutex()

    /**
     * This cache will speed up session lookups but the real reason is that Ktor
     * performs write even when there is no change in session data. That would
     * mean very frequent and unnecessary SQL updates, not to mention that
     * PostgreSQL complains about concurrent updates a lot.
     */
    private val cache = mutableMapOf<String, SessionCacheEntry>()

    override suspend fun invalidate(id: String) {
        transaction {
            SessionTable.deleteWhere { SessionTable.id eq id }
            cache.remove(id)
        }
    }

    override suspend fun <R> read(id: String, consumer: suspend (ByteReadChannel) -> R): R {

        // check the cache first, return with the cached content if there is one
        val cached = mutex.withLock {
            sendForUpdate(cache[id])
        }

        if (cached != null) {
            return consumer(ByteReadChannel(cached.sessionData))
        }

        // check SQL, in case of server restart
        val entry = transaction {
            SessionTable
                .select { SessionTable.id eq id }
                .map {
                    SessionCacheEntry(
                        id,
                        it[SessionTable.content].bytes,
                        it[SessionTable.lastAccess].toKotlinInstant()
                    )
                }
                .firstOrNull()
        } ?: throw NoSuchElementException()

        // update the cache
        mutex.withLock {
            cache[id] = sendForUpdate(entry) !! // cannot be null at this point
        }

        return consumer(ByteReadChannel(entry.sessionData))
    }

    private suspend fun sendForUpdate(entry: SessionCacheEntry?): SessionCacheEntry? {
        if (entry == null) return entry

        val now = Clock.System.now()
        val cutoff = now.minus(settings.updateDelay, DateTimeUnit.SECOND)

        println("$cutoff ${entry.createdAt} ${entry.createdAt.epochSeconds < cutoff.epochSeconds}")

        if (entry.createdAt.epochSeconds < cutoff.epochSeconds) {
            val newEntry = SessionCacheEntry(entry.sessionId, entry.sessionData, now)
            cache[entry.sessionId] = newEntry
            SessionMaintenanceTask.updateChannel.send(newEntry)
            return newEntry
        }

        return entry
    }

    override suspend fun write(id: String, provider: suspend (ByteWriteChannel) -> Unit) {
        provider(CoroutineScope(Dispatchers.IO).reader(coroutineContext, autoFlush = true) {
            val content = channel.readAvailable()

            // I'm not sure this covers all the corner cases when the SQL update is not successful
            // TODO think about session SQL consistency

            mutex.withLock {
                val cached = cache[id]

                if (cached != null) {
                    // if the new value is the same as the cached one, simply skip write
                    if (cached.sessionData.contentEquals(content)) return@reader

                    // save the new value into the cache, this will prevent other writes
                    cache[id] = SessionCacheEntry(id, content, cached.createdAt)
                }
            }

            // TODO use upsert. It is not supported out-of-the-box by exposed so, I went for a manual check.

            val now = Clock.System.now().toJavaInstant()

            transaction {

                val session = SessionTable.select { SessionTable.id eq id }.firstOrNull()

                if (session == null) {
                    SessionTable.insert {
                        it[SessionTable.id] = id
                        it[SessionTable.content] = ExposedBlob(content)
                        it[lastAccess] = now
                    }
                } else {
                    SessionTable.update({ SessionTable.id eq id }) {
                        it[SessionTable.content] = ExposedBlob(content)
                        it[lastAccess] = now
                    }
                }
            }

        }.channel)
    }

    private suspend fun ByteReadChannel.readAvailable(): ByteArray {
        val data = ByteArrayOutputStream()
        val temp = ByteArray(1024)
        while (! isClosedForRead) {
            val read = readAvailable(temp)
            if (read <= 0) break
            @Suppress("BlockingMethodInNonBlockingContext") // it is a friggin byte array
            data.write(temp, 0, read)
        }
        return data.toByteArray()
    }

}