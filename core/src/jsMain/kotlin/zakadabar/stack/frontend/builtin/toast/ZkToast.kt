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
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

/**
 * A toast to show a message to the user.
 *
 * @property  text        The text to display. When null, [content] should be set.
 * @property  content     The content to display. When null, [text] should be set.
 * @property  flavour     Flavour of the toast, sets coloring.
 * @property  hideAfter   Number of milliseconds after the toast is automatically hidden.
 * @property  icon        The icon to use in the toast.
 * @property  iconClass   The CSS class to add to the icon.
 * @property  innerClass  The CSS class to add to the inner container.
 */
open class ZkToast(
    val text: String? = null,
    val content: ZkElement? = null,
    val flavour: ZkFlavour = ZkFlavour.Success,
    val hideAfter: Long? = null,
    val icon: ZkElement? = null,
    val iconClass: String? = null,
    val innerClass: String? = null
) : ZkElement() {

    companion object {
        var autoHideDefaults = mutableMapOf<ZkFlavour, Long>(
            ZkFlavour.Primary to 0,
            ZkFlavour.Secondary to 3000,
            ZkFlavour.Success to 3000,
            ZkFlavour.Warning to 0,
            ZkFlavour.Danger to 0,
            ZkFlavour.Info to 0,
            ZkFlavour.Custom to 0
        )
    }

    @Suppress("DuplicatedCode") // no idea how to bring these two together
    override fun onCreate() {
        classList += zkToastStyles.toastOuter

        val finalIcon: ZkElement
        val finalInnerClass: String
        val finalIconClass: String

        when (flavour) {
            ZkFlavour.Primary -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.info)
                finalIconClass = iconClass ?: zkToastStyles.primaryIcon
                finalInnerClass = innerClass ?: zkToastStyles.primaryInner
            }
            ZkFlavour.Secondary -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.info)
                finalIconClass = iconClass ?: zkToastStyles.secondaryIcon
                finalInnerClass = innerClass ?: zkToastStyles.secondaryInner
            }
            ZkFlavour.Success -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.checkCircle)
                finalIconClass = iconClass ?: zkToastStyles.successIcon
                finalInnerClass = innerClass ?: zkToastStyles.successInner
            }
            ZkFlavour.Warning -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.warningAmber)
                finalIconClass = iconClass ?: zkToastStyles.warningIcon
                finalInnerClass = innerClass ?: zkToastStyles.warningInner
            }
            ZkFlavour.Danger -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.report)
                finalIconClass = iconClass ?: zkToastStyles.dangerIcon
                finalInnerClass = innerClass ?: zkToastStyles.dangerInner
            }
            ZkFlavour.Info -> {
                finalIcon = icon ?: ZkIcon(ZkIcons.info)
                finalIconClass = iconClass ?: zkToastStyles.infoIcon
                finalInnerClass = innerClass ?: zkToastStyles.infoInner
            }
            else -> {
                finalIcon = requireNotNull(icon) { "toast icon cannot be null when flavour is Custom" }
                finalIconClass = requireNotNull(iconClass) { "toast iconClass cannot be null when flavour is Custom" }
                require(finalIconClass.isNotBlank()) { "toast iconClass cannot be blank when flavour is Custom" }
                finalInnerClass = requireNotNull(innerClass) { "toast innerClass cannot be null when flavour is Custom" }
                require(finalInnerClass.isNotBlank()) { "toast innerClass cannot be blank when flavour is Custom" }
            }
        }

        + div(zkToastStyles.toastInner) {

            classList += finalInnerClass

            + div(zkToastStyles.iconContainer) {
                classList += finalIconClass
                + finalIcon
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

        val finalHideAfter = hideAfter ?: autoHideDefaults[flavour] ?: 0L

        if (finalHideAfter != 0L) {
            io {
                delay(finalHideAfter)
                dispose()
            }
        }

        return this
    }

    open fun dispose() {
        ZkApplication.toasts -= this
    }
}