/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.pages

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkPage

class ExamplePage : ZkPage() {

    override fun onConfigure() {
        appTitle = false
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