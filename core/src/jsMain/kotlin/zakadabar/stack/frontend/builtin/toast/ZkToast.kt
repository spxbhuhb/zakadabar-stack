/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package zakadabar.stack.frontend.builtin.toast

import kotlinx.coroutines.delay
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.builtin.misc.NYI
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

/**
 * A toast to show a message to the user.
 *
 * @property  text      The text to display. When null, [content] should be set.
 * @property  content   The content to display. When null, [text] should be set.
 * @property  flavour   Flavour of the toast, sets coloring.
 * @property  hideAfter Number of milliseconds after the toast is automatically hidden.
 */
open class ZkToast(
    val text: String? = null,
    val content: ZkElement? = null,
    val flavour: ZkFlavour = ZkFlavour.Success,
    val hideAfter: Long? = if (flavour == ZkFlavour.Danger || flavour == ZkFlavour.Warning) null else 3000
) : ZkElement() {

    override fun onCreate() {
        classList += zkToastStyles.toastOuter

        val icon: ZkIcon
        val innerClass: String
        val iconClass: String

        when (flavour) {
            ZkFlavour.Primary -> {
                icon = ZkIcon(ZkIcons.info)
                iconClass = zkToastStyles.primaryIcon
                innerClass = zkToastStyles.primaryInner
            }
            ZkFlavour.Secondary -> {
                icon = ZkIcon(ZkIcons.info)
                iconClass = zkToastStyles.secondaryIcon
                innerClass = zkToastStyles.secondaryInner
            }
            ZkFlavour.Success -> {
                icon = ZkIcon(ZkIcons.checkCircle)
                iconClass = zkToastStyles.successIcon
                innerClass = zkToastStyles.successInner
            }
            ZkFlavour.Warning -> {
                icon = ZkIcon(ZkIcons.warningAmber)
                iconClass = zkToastStyles.warningIcon
                innerClass = zkToastStyles.warningInner
            }
            ZkFlavour.Danger -> {
                icon = ZkIcon(ZkIcons.errorOutline)
                iconClass = zkToastStyles.dangerIcon
                innerClass = zkToastStyles.dangerInner
            }
            ZkFlavour.Info -> {
                icon = ZkIcon(ZkIcons.info)
                iconClass = zkToastStyles.infoIcon
                innerClass = zkToastStyles.infoInner
            }
            else -> {
                + NYI()
                return
            }
        }

        + div(zkToastStyles.toastInner) {

            classList += innerClass

            + div(zkToastStyles.iconContainer) {
                classList += iconClass
                + icon
            }

            text?.let {
                + div(zkToastStyles.text) {
                    + it
                }
            }

            content?.let { + it marginRight 16 }

            + ZkIconButton(ZkIcons.close, cssClass = zkToastStyles.closeIcon) { ZkApplication.toasts -= this }

        }
    }

    open fun run(): ZkToast {
        ZkApplication.toasts += this
        if (hideAfter != null) {
            io {
                delay(hideAfter)
                dispose()
            }
        }
        return this
    }

    open fun dispose() {
        ZkApplication.toasts -= this
    }
}