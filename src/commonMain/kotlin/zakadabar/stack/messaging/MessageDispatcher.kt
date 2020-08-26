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

import kotlin.reflect.KClass

/**
 * Interface for message dispatchers.
 */
interface MessageDispatcher {

    val trace: Int

    fun <T : Message> subscribe(messageClass: KClass<T>, receiver: (T) -> Unit)

    fun <T : Message> unsubscribe(messageType: KClass<T>, receiver: (T) -> Unit)

    fun <T : Message> postSync(build: () -> T)

    suspend fun <T : Message> post(message: T)

}