/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.builtin.toast.ZkToast
import zakadabar.stack.frontend.builtin.toast.ZkToastType

/**
 * Displays a message to the user by showing a toast. Custom toast type should use it's own
 * function.
 */
fun toast(info: Boolean = false, warning: Boolean = false, error: Boolean = false, message: () -> String) {
    val type = when {
        info -> ZkToastType.Info
        warning -> ZkToastType.Warning
        error -> ZkToastType.Error
        else -> ZkToastType.Success
    }

    Application.toasts += ZkToast(message(), type)
}