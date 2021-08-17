/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.pages

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.page.ZkPage

class ExamplePage : ZkPage() {

    override fun onConfigure() {
        setAppTitle = false
    }

    override fun onCreate() {
        + "The content of the page."
    }

}

class PageExample(
    element: HTMLElement
) : ZkElement(element) {

    val container = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + div(zkLayoutStyles.block) {
            + ExamplePage()
        }
    }
}