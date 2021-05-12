/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.resources.ZkFlavour

fun primaryToast(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Primary, hideAfter = hideAfter).run()
fun secondaryToast(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Secondary, hideAfter = hideAfter).run()
fun successToast(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Success, hideAfter = hideAfter).run()
fun warningToast(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Warning, hideAfter = hideAfter).run()
fun dangerToast(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Danger, hideAfter = hideAfter).run()
fun infoToast(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Info, hideAfter = hideAfter).run()