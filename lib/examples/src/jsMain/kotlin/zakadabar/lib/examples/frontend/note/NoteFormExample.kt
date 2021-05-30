/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.note

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.note.noteWarning
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.util.default

/**
 * This example shows how to create a note with a compex content.
 */
class NoteFormExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {

            + noteWarning("Complex Note Content", InlineForm())

        }
    }

    class InlineForm : ZkForm<BuiltinBo>() {

        override fun onConfigure() {
            bo = default { }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            super.onCreate()

            element.style.padding = "20px"

            + section("This is a form in a note!") {
                + bo::id
                + bo::doubleValue
                + bo::instantValue
                + bo::optInstantValue
                + bo::stringValue
            }
        }
    }
}