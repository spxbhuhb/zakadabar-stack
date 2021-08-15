/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.icon

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px
import zakadabar.core.resource.iconSources
import zakadabar.core.resource.theme
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.browser.util.plusAssign

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