/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package zakadabar.stack.frontend.builtin.toast

import kotlinx.coroutines.delay
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.marginRight
import zakadabar.stack.frontend.elements.plusAssign
import zakadabar.stack.frontend.resources.Icons
import zakadabar.stack.frontend.util.launch

/**
 * A toast to show a message to the user.
 */
open class ZkToast(
    val message: String,
    val type: ZkToastType
) : ZkElement() {

    override fun init() = build {
        className = ZkToastStyles.toast

        when (type) {
            ZkToastType.Info -> classList += ZkToastStyles.info
            ZkToastType.Success -> classList += ZkToastStyles.success
            ZkToastType.Warning -> classList += ZkToastStyles.warning
            ZkToastType.Error -> classList += ZkToastStyles.error
            ZkToastType.Custom -> Unit
        }

        + div { + message } marginRight 16
        + ZkIconButton(Icons.close) { Application.toasts -= this }

        if (type == ZkToastType.Success || type == ZkToastType.Info) {
            launch {
                delay(3000)
                Application.toasts -= this
            }
        }
    }
}