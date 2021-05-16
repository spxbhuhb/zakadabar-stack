/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.layout

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.successButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.builtin.note.successNote
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.marginBottom

class TabContainerExample(
    element: HTMLElement
) : ZkElement(element) {

    val container = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + div(zkLayoutStyles.block) {
            + "text1"
            ! """<span style="font-size: 150%">text2</span>"""
            + (document.createElement("input") as HTMLElement) marginBottom 20

            + container

            container += successButton("A Button, click to hide the note") {
                container -= ZkNote::class
            } marginBottom 20

            container += successNote("A Note", "This note will disappear shortly.")
        }
    }





    class InlineForm : ZkForm<BuiltinDto>() {

        override fun onConfigure() {
            dto = default { }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            super.onCreate()

            element.style.margin = "20px"

            + section("This is a form in a toast!") {
                + dto::id
                + dto::doubleValue
                + dto::instantValue
                + dto::optInstantValue
                + dto::stringValue
            }
        }
    }
}