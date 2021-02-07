/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

import kotlinx.serialization.KSerializer

abstract class ActionDtoCompanion<RESPONSE : Any> {

    abstract fun serializer(): KSerializer<RESPONSE>

    lateinit var comm: () -> ActionCommInterface
}