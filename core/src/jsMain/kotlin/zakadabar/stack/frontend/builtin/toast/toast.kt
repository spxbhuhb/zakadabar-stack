/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.resources.ZkFlavour

/**
 * Displays a message to the user by showing a toast. Custom toast type should use it's own
 * function.
 */
fun toast(info: Boolean = false, warning: Boolean = false, danger: Boolean = false, hideAfter: Long? = null, message: () -> String): ZkToast {
    val type = when {
        info -> ZkFlavour.Info
        warning -> ZkFlavour.Warning
        danger -> ZkFlavour.Danger
        else -> ZkFlavour.Success
    }

    val ha = hideAfter ?: when {
        warning -> null
        danger -> null
        else -> 3000
    }

    val toast = ZkToast(message(), null, type, ha)
    toast.run()
    return toast
}

fun primaryToast(message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Primary, hideAfter = 3000).run()
fun secondaryToast(message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Secondary, hideAfter = 3000).run()
fun successToast(message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Success, hideAfter = 3000).run()
fun warningToast(message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Warning).run()
fun dangerToast(message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Danger).run()
fun infoToast(message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Info, hideAfter = 3000).run()



