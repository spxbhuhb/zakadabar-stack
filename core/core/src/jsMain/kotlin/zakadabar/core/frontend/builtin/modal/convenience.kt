/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.modal

import zakadabar.core.resources.localizedStrings

/**
 * Shows a confirmation dialog and executes the function only if the
 * user confirmed the action.
 *
 * @return  true if the the action was executed, false if not
 */
suspend fun withConfirm(
    title : String = localizedStrings.confirmation,
    message : String =  localizedStrings.confirmDelete,
    func : () -> Unit
) : Boolean {
    val confirmed = ZkConfirmDialog(title, message).run()
    if (confirmed) {
        func()
    }
    return confirmed
}