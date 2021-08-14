/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.button

import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.toast.toastSuccess
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIcons
import zakadabar.core.frontend.resources.css.px
import zakadabar.core.frontend.resources.theme
import zakadabar.core.frontend.util.marginBottom

/**
 * This example shows how to create buttons.
 */
class ButtonExamples(
    element: HTMLElement,
    val flavour: ZkFlavour
) : ZkElement(element) {


    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "repeat(5,max-content)"
            gridGap = (theme.spacingStep / 2).px

            + "Text"
            + ZkButton("Default", flavour) { onButtonClick() }
            + ZkButton("No Fill", flavour, fill = false) { onButtonClick() }
            + ZkButton("No Border", flavour, border = false) { onButtonClick() }
            + ZkButton("No Fill No Border", flavour, fill = false, border = false) { onButtonClick() }

            + "Icon"
            + ZkButton(ZkIcons.info, flavour) { onButtonClick() }
            + ZkButton(ZkIcons.info, flavour, fill = false) { onButtonClick() }
            + ZkButton(ZkIcons.info, flavour, border = false) { onButtonClick() }
            + ZkButton(ZkIcons.info, flavour, fill = false, border = false) { onButtonClick() }

            + "Icon Round"
            + ZkButton(ZkIcons.cloudUpload, flavour, round = true) { onButtonClick() }
            + ZkButton(ZkIcons.cloudUpload, flavour, round = true, fill = false) { onButtonClick() }
            + ZkButton(ZkIcons.cloudUpload, flavour, round = true, border = false) { onButtonClick() }
            + ZkButton(ZkIcons.cloudUpload, flavour, round = true, fill = false, border = false) { onButtonClick() }

            + "Combined"
            + ZkButton("Default", ZkIcons.info, flavour) { onButtonClick() }
            + ZkButton("No Fill", ZkIcons.info, flavour, fill = false) { onButtonClick() }
            + ZkButton("No Border", ZkIcons.info, flavour, border = false) { onButtonClick() }
            + ZkButton("No Fill No Border", ZkIcons.info, flavour, fill = false, border = false) { onButtonClick() }

        } marginBottom theme.spacingStep

    }

    private fun onButtonClick() {
        toastSuccess { "You clicked on a button!" }
    }
}