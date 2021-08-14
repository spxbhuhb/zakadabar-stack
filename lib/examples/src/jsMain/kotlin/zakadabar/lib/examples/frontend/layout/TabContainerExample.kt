/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.layout

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.resources.strings
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.layout.tabcontainer.ZkTabContainer
import zakadabar.core.frontend.builtin.note.noteInfo
import zakadabar.core.frontend.builtin.toast.toastSuccess
import zakadabar.core.frontend.resources.css.px
import zakadabar.core.frontend.util.default

class TabContainerExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + ZkTabContainer {

            height = 400.px

            + tab("First Tab") {
                (0..10).forEach { _ -> + p { + strings.loremIpsum } }
            }

            + tab("Second Tab") {
                + noteInfo("Info", "You are looking at the content of the second tab.")
            }

            + tab(InlineForm())

        }

        this marginBottom 20
    }

    class InlineForm : ZkForm<BuiltinBo>() {

        override fun onConfigure() {
            bo = default { }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            super.onCreate()

            build("Inline Form") {
                + section("This is a form in a tab") {
                    + bo::id
                    + bo::doubleValue
                    + bo::instantValue
                    + bo::optInstantValue
                    + bo::stringValue
                }
            }
        }

        override fun validate(submit: Boolean): Boolean {
            super.validate(submit)
            toastSuccess { "You clicked on save!" }
            return false
        }
    }
}