/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.server.ktor

import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

class ExpiredToNewEntry(
    val newSessionId: String,
    val createdAt: Instant
)

internal val SessionIdKey = AttributeKey<String>("SessionId")
internal val LoginTimeoutKey = AttributeKey<Boolean>("LoginTimeout")
internal val expiredMapMutex = Mutex()
internal val expiredToNew = mutableMapOf<String, ExpiredToNewEntry>() // TODO this assumes that there is only one server, should persist this mapping (maybe)

/**
 * A session tracker that is able to handle session renewals. A cut & paste from Ktor's
 * [SessionTrackerById].
 *
 * The cookie session provider calls the tracker to load the session. When there is no session for the
 * given session key the tracker:
 *
 * 1) generates a new session key
 * 2) generates a new session object
 * 3) puts the old session id into the "OldSessionId" attribute.
 */
class RenewableSessionTrackerById<S : Any>(
    private val type: KClass<S>,
    private val serializer: SessionSerializer<S>,
    private val storage: SessionStorage,
    val sessionIdProvider: () -> String
) : SessionTracker<S> {

    override suspend fun load(call: ApplicationCall, transport: String?): S? {
        val sessionId = transport ?: return null

        call.attributes.put(SessionIdKey, sessionId)

        try {
            return read(sessionId)
        } catch (notFound: NoSuchElementException) {
            call.application.log.debug("Failed to lookup session: $notFound")
        }

        call.attributes.put(LoginTimeoutKey, true)

        expiredMapMutex.withLock {
            val mapped = expiredToNew.getOrPut(sessionId) { ExpiredToNewEntry(sessionIdProvider(), Clock.System.now()) }
            call.attributes.put(SessionIdKey, mapped.newSessionId)
        }

        return null
    }

    private suspend fun read(sessionId: String) = storage.read(sessionId) { channel ->
        val text = channel.readUTF8Line() ?: throw IllegalStateException("Failed to read stored session from $channel")
        serializer.deserialize(text)
    }

    override suspend fun store(call: ApplicationCall, value: S): String {
        val sessionId = call.attributes.computeIfAbsent(SessionIdKey, sessionIdProvider)
        val serialized = serializer.serialize(value)
        storage.write(sessionId) { channel ->
            channel.writeStringUtf8(serialized)
            channel.close()
        }
        return sessionId
    }

    override suspend fun clear(call: ApplicationCall) {
        val sessionId = call.attributes.takeOrNull(SessionIdKey)
        if (sessionId != null) {
            storage.invalidate(sessionId)
        }
    }

    override fun validate(value: S) {
        if (! type.javaObjectType.isAssignableFrom(value.javaClass)) {
            throw IllegalArgumentException("Value for this session tracker expected to be of type $type but was $value")
        }
    }

    override fun toString(): String {
        return "RenewableSessionTrackerById: $storage"
    }
}
