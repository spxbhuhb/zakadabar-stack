/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.misc

import kotlinx.serialization.Serializable

/**
 * A secret string that should not be shown anywhere.
 * [toString] returns with asterisks, so it is safe to use it in logs.
 * Be careful with serialization as it does contain the plain value.
 */
@Serializable
class Secret(
    var value: String
) {
    override fun toString(): String {
        return "********"
    }
}