/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.modal

import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.util.io
import zakadabar.core.resource.localizedStrings
import zakadabar.core.text.capitalized

open class ZkConfirmDialog(
    open val title: String? = null,
    open val message: String,
    open val noLabel: String = localizedStrings.no.capitalized(),
    open val yesLabel: String = localizedStrings.yes.capitalized(),
) : ZkModalBase<Boolean>() {

    override fun onCreate() {
        titleText = title
        super.onCreate()
    }

    override fun buildContent() {
        + message
    }

    override fun buildButtons() {
        + ZkButton(noLabel, onClick = ::onNo)
        + ZkButton(yesLabel, onClick = ::onYes)
    }

    open fun onNo() = io {
        channel.send(false)
    }

    open fun onYes() = io {
        channel.send(true)
    }
}