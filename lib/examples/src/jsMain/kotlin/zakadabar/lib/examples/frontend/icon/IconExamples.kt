/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.icon

import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.icon.ZkIcon
import zakadabar.core.frontend.resources.css.ZkCssStyleSheet
import zakadabar.core.frontend.resources.css.cssStyleSheet
import zakadabar.core.frontend.resources.css.px
import zakadabar.core.frontend.resources.iconSources
import zakadabar.core.frontend.resources.theme
import zakadabar.core.frontend.util.marginBottom
import zakadabar.core.frontend.util.plusAssign

/**
 * This example shows how to create icons.
 */
class IconExamples(
    element: HTMLElement
) : ZkElement(element) {

    companion object {
        val iconExampleStyles by cssStyleSheet(IconExampleStyles())

        class IconExampleStyles : ZkCssStyleSheet() {
            val exampleStyles by cssClass {
                fill = theme.textColor
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        + grid {
            gridTemplateColumns = "repeat(6,max-content)"
            gridGap = theme.spacingStep.px

            classList += iconExampleStyles.exampleStyles

            iconSources.values.forEach {
                + it.name
                + ZkIcon(it, size = 22) marginRight theme.spacingStep
            }


        } marginBottom theme.spacingStep

    }

}