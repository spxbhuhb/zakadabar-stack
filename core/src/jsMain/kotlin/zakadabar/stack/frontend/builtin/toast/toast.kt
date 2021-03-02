/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.application.Application

/**
 * Displays a message to the user by showing a toast. Custom toast type should use it's own
 * function.
 */
fun toast(info: Boolean = false, warning: Boolean = false, error: Boolean = false, hideAfter: Long? = null, message: () -> String): ZkToast {
    val type = when {
        info -> ZkToastType.Info
        warning -> ZkToastType.Warning
        error -> ZkToastType.Error
        else -> ZkToastType.Success
    }

    return ZkToast(message(), type, hideAfter).also { Application.toasts += it }
}