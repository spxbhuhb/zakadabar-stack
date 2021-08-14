/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.modal

import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.util.io
import zakadabar.core.resources.localizedStrings
import zakadabar.core.text.capitalized

open class ZkMessageDialog(
    open val title: String? = null,
    open val message: String,
    open val okLabel: String = localizedStrings.ok.capitalized(),
) : ZkModalBase<Boolean>() {

    override fun onCreate() {
        titleText = title
        super.onCreate()
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