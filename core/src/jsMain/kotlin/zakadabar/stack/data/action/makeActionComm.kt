/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

actual fun makeActionComm(companion: ActionBoCompanion<*>): ActionCommInterface {
    return ActionComm(companion)
}