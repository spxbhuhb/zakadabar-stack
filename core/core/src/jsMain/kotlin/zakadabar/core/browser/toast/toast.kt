/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.toast

import zakadabar.core.resource.ZkFlavour

fun toastPrimary(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Primary, hideAfter = hideAfter).run()
fun toastSecondary(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Secondary, hideAfter = hideAfter).run()
fun toastSuccess(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Success, hideAfter = hideAfter).run()
fun toastWarning(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Warning, hideAfter = hideAfter).run()
fun toastDanger(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Danger, hideAfter = hideAfter).run()
fun toastInfo(hideAfter: Long? = null, message: () -> String) = ZkToast(message(), flavour = ZkFlavour.Info, hideAfter = hideAfter).run()