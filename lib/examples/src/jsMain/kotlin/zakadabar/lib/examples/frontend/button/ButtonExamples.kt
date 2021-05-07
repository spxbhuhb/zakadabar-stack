/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.button

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.ZkApplication.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.marginBottom

/**
 * This example shows how to create checkbox lists.
 */
class ButtonExamples(
    element: HTMLElement,
    val flavour: ZkFlavour
) : ZkElement(element) {


    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "repeat(5,max-content)"
            gridGap = theme.layout.spacingStep / 2

            + "Text"
            + ZkButton("Default", flavour)
            + ZkButton("No Fill", flavour, fill = false)
            + ZkButton("No Border", flavour, border = false)
            + ZkButton("No Fill No Border", flavour, fill = false, border = false)

            + "Icon"
            + ZkButton(ZkIcons.info, flavour)
            + ZkButton(ZkIcons.info, flavour, fill = false)
            + ZkButton(ZkIcons.info, flavour, border = false)
            + ZkButton(ZkIcons.info, flavour, fill = false, border = false)

            + "Icon Round"
            + ZkButton(ZkIcons.info, flavour, round = true)
            + ZkButton(ZkIcons.info, flavour, round = true, fill = false)
            + ZkButton(ZkIcons.info, flavour, round = true, border = false)
            + ZkButton(ZkIcons.info, flavour, round = true, fill = false, border = false)

            + "Combined"
            + ZkButton("Default", ZkIcons.info, flavour)
            + ZkButton("No Fill", ZkIcons.info, flavour, fill = false)
            + ZkButton("No Border", ZkIcons.info, flavour, border = false)
            + ZkButton("No Fill No Border", ZkIcons.info, flavour, fill = false, border = false)

        } marginBottom theme.layout.spacingStep

    }

}