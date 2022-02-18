/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.other

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.other.ZkChip
import zakadabar.core.browser.other.zkChipStyles
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.px

class ExampleChip(element: HTMLElement) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        all(true, true)
        all(false, true)
        all(false, false)
    }

    fun all(fill: Boolean, border: Boolean) {
        + row {
            + ZkChip("primary", fill = fill, border = border) css zkChipStyles.gap
            + ZkChip("secondary", fill = fill, border = border, flavour = ZkFlavour.Secondary) css zkChipStyles.gap
            + ZkChip("info", fill = fill, border = border, flavour = ZkFlavour.Info) css zkChipStyles.gap
            + ZkChip("success", fill = fill, border = border, flavour = ZkFlavour.Success)
        } marginBottom 10.px

        + row {
            + ZkChip("warning", fill = fill, border = border, flavour = ZkFlavour.Warning) css zkChipStyles.gap
            + ZkChip("danger", fill = fill, border = border, flavour = ZkFlavour.Danger) css zkChipStyles.gap
            + ZkChip("disabled", fill = fill, border = border, flavour = ZkFlavour.Disabled)
        } marginBottom 10.px
    }
}

class ExampleChipWithButton(element: HTMLElement) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        all(true, true)
        all(false, true)
        all(false, false)
    }

    fun all(fill: Boolean, border: Boolean) {
        + row {
            + ZkChip("primary", fill = fill, border = border, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("secondary", fill = fill, border = border, flavour = ZkFlavour.Secondary, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("info", fill = fill, border = border, flavour = ZkFlavour.Info, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("success", fill = fill, border = border, flavour = ZkFlavour.Success, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } }
        } marginBottom 10.px

        + row {
            + ZkChip("warning", fill = fill, border = border, flavour = ZkFlavour.Warning, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("danger", fill = fill, border = border, flavour = ZkFlavour.Danger, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("disabled", fill = fill, border = border, flavour = ZkFlavour.Disabled, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } }
        } marginBottom 10.px
    }
}

class ExampleChipWithIcon(element: HTMLElement) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        all(true, true)
        all(false, true)
        all(false, false)
    }

    fun all(fill: Boolean, border: Boolean) {
        + row {
            + ZkChip("primary", icon = ZkIcons.globe, fill = fill, border = border) css zkChipStyles.gap
            + ZkChip("secondary", icon = ZkIcons.globe, fill = fill, border = border, flavour = ZkFlavour.Secondary) css zkChipStyles.gap
            + ZkChip("info", icon = ZkIcons.globe, fill = fill, border = border, flavour = ZkFlavour.Info) css zkChipStyles.gap
            + ZkChip("success", icon = ZkIcons.globe, fill = fill, border = border, flavour = ZkFlavour.Success)
        } marginBottom 10.px

        + row {
            + ZkChip("warning", icon = ZkIcons.globe, fill = fill, border = border, flavour = ZkFlavour.Warning) css zkChipStyles.gap
            + ZkChip("danger", icon = ZkIcons.globe, fill = fill, border = border, flavour = ZkFlavour.Danger) css zkChipStyles.gap
            + ZkChip("disabled", icon = ZkIcons.globe, fill = fill, border = border, flavour = ZkFlavour.Disabled)
        } marginBottom 10.px
    }
}

class ExampleChipWithIconAndButton(element: HTMLElement) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        all(true, true)
        all(false, true)
        all(false, false)
    }

    fun all(fill: Boolean, border: Boolean) {
        + row {
            + ZkChip("primary", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("secondary", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border, flavour = ZkFlavour.Secondary) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("info", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border, flavour = ZkFlavour.Info) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("success", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border, flavour = ZkFlavour.Success) { toastSuccess { "click" } }
        } marginBottom 10.px

        + row {
            + ZkChip("warning", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border, flavour = ZkFlavour.Warning) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("danger", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border, flavour = ZkFlavour.Danger) { toastSuccess { "click" } } css zkChipStyles.gap
            + ZkChip("disabled", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon, fill = fill, border = border, flavour = ZkFlavour.Disabled) { toastSuccess { "click" } }
        } marginBottom 10.px
    }
}