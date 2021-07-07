/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.action

/**
 * Run an action without having the action class. Intended for framework use as this is
 * not type safe.
 */
actual suspend fun runAction(namespace: String, type: String, data: String): String {
    TODO("Not yet implemented")
}