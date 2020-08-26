/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.messaging

import zakadabar.stack.util.stdoutTrace
import kotlin.reflect.KClass


/**
 * A simple message dispatcher that calls all subscribers sequentially and blocks
 * execution until everything is done. Use it for short, fast, synchronized event
 * distribution.
 */
class SyncMessageDispatcher : MessageDispatcher {

    private val subscriptions = mutableMapOf<KClass<*>, Subscribers<Message>>()

    override val trace = 0

    override fun <T : Message> subscribe(messageClass: KClass<T>, receiver: (T) -> Unit) {
        // SYNC getOrPut is not atomic
        val subscribers = subscriptions.getOrPut(messageClass) { mutableListOf() }

        @Suppress("UNCHECKED_CAST") // Can't be helped. Method signature ensures type safety.
        subscribers as Subscribers<T>

        subscribers += receiver
    }

    override fun <T : Message> unsubscribe(messageType: KClass<T>, receiver: (T) -> Unit) {
        subscriptions[messageType]?.remove(receiver)
    }

    override fun <T : Message> postSync(build: () -> T) {
        val message = build()

        if (trace > 50) stdoutTrace("dispatcher.postSync", message.toString())

        val subscribers = subscriptions[message::class] ?: return // simple return when there are no subscriptions

        @Suppress("UNCHECKED_CAST") // Can't be helped. Subscribe ensures type safety.
        subscribers as Subscribers<T>

        subscribers.forEach { it(message) }
    }

    override suspend fun <T : Message> post(message: T) {
        throw NotImplementedError("sync message dispatcher cannot handle async posting")
    }

}