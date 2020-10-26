/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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