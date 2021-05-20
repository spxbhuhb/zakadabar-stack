/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.util.io

open class ZkMessageDialog(
    open val title: String? = null,
    open val message: String,
    open val okLabel: String = stringStore.ok.capitalize(),
) : ZkModalBase<Boolean>() {

    override fun onCreate() {
        super.onCreate()
        build(title)
    }

    override fun buildContent() {
        + message
    }

    override fun buildButtons() {
        + ZkButton(okLabel, onClick = ::onOk)
    }

    open fun onOk() = io {
        channel.send(true)
    }
}