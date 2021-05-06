/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import kotlinx.coroutines.channels.Channel
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement

open class ZkModalBase<T : Any> : ZkElement() {

    protected val channel = Channel<T>()

    open suspend fun run(): T {
        ZkApplication.modals.show()
        ZkApplication.modals += this

        val value = channel.receive()

        ZkApplication.modals -= this
        ZkApplication.modals.hide()

        return value
    }

}