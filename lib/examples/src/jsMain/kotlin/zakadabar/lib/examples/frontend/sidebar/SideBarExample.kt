/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.sidebar

import org.w3c.dom.HTMLElement
import zakadabar.lib.markdown.frontend.markdownStyles
import zakadabar.core.browser.application.application
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPathPage
import zakadabar.core.browser.sidebar.ZkSideBar
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.localizedStrings


object ExampleSideBarTarget : ZkPathPage() {
    override fun onCreate() {
        super.onCreate()
        + column {
            + "The path is: $path"
            + "Click on the button to go back."
            + buttonPrimary(localizedStrings.back) { application.back() }
        }
    }
}

class ExampleSideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + group(ExampleSideBarTarget, "group label") {
            + item("item 1.1") { toastSuccess { "Click on 1.1" } }
            + item("item 1.2") { toastSuccess { "Click on 1.2" } }
        }

        + section("section") {
            + item("item 2.1") { toastSuccess { "Click on 2.1" } }
            + item("item 2.2") { toastSuccess { "Click on 2.2" } }
        }

        + item(ExampleSideBarTarget)

    }

}

class SideBarExample(
    element: HTMLElement
) : ZkElement(element) {

    val container = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + div(markdownStyles.unMarkdown) {

            classList += zkLayoutStyles.fixBorder

            + ExampleSideBar()
        }
    }
}