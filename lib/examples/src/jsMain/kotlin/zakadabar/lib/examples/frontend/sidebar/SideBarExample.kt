/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.sidebar

import org.w3c.dom.HTMLElement
import zakadabar.lib.markdown.frontend.markdownStyles
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.buttonPrimary
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPathPage
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBar
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.util.plusAssign


object ExampleSideBarTarget : ZkPathPage() {
    override fun onCreate() {
        super.onCreate()
        + column {
            + "The path is: $path"
            + "Click on the button to go back."
            + buttonPrimary(stringStore.back) { application.back() }
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