/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.tabcontainer.background

import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.note.noteInfo
import zakadabar.core.browser.tabcontainer.ZkTabContainerStyles
import zakadabar.core.browser.tabcontainer.tabContainer
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.resource.ZkColors
import zakadabar.core.resource.css.cssParameter
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px

class ExampleStyles : ZkTabContainerStyles() {
    override var tabBackgroundColor by cssParameter { "transparent" }

    val exampleBackground by cssClass {
        backgroundColor = ZkColors.Yellow.c400
    }
}

private var exampleStyles by cssStyleSheet(ExampleStyles())

class TabContainerBackground : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + p { + "The default is to use the theme background." } marginBottom 20

        + div(exampleStyles.exampleBackground) {

            + zkLayoutStyles.p1

            + tabContainer {

                height = 400.px

                + tab("First Tab") {
                    (0..10).forEach { _ -> + p { + strings.loremIpsum } }
                }

                + tab("Second Tab") {
                    + noteInfo("Info", "You are looking at the content of the second tab.")
                }
            }
        } marginBottom 20

        + p { + "This can be overridden by setting the tab background to transparent." } marginBottom 20

        + div(exampleStyles.exampleBackground) {

            + zkLayoutStyles.p1

            + tabContainer(exampleStyles) {

                height = 400.px

                + tab("First Tab") {
                    (0..10).forEach { _ -> + p { + strings.loremIpsum } }
                }

                + tab("Second Tab") {
                    + noteInfo("Info", "You are looking at the content of the second tab.")
                }
            }
        } marginBottom 20

    }

}