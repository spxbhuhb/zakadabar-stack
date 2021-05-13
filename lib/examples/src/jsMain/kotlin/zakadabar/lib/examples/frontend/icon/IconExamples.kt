/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.icon

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet
import zakadabar.stack.frontend.resources.iconSources
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign

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
            gridGap = theme.spacingStep

            classList += iconExampleStyles.exampleStyles

            iconSources.values.forEach {
                + it.name
                + ZkIcon(it, size = 22) marginRight theme.spacingStep
            }


        } marginBottom theme.spacingStep

    }

}