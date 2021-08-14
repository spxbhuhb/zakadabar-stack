/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.modal

import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.modal.ZkModalBase
import zakadabar.core.frontend.util.io

open class MyMessageDialog : ZkModalBase<String>() {

    override fun buildTitle() {
        + "This is manually built title."
    }

    override fun buildContent() {
        + "This is my message dialog."
    }

    override fun buildButtons() {
        + ZkButton("I will use translated strings instead", onClick = ::onOk)
    }

    open fun onOk() = io {
        channel.send("You promised!")
    }
}