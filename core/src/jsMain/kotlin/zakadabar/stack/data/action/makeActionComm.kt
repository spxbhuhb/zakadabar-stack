/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

actual fun makeActionComm(companion: ActionDtoCompanion<*>): ActionCommInterface {
    return ActionComm(companion)
}