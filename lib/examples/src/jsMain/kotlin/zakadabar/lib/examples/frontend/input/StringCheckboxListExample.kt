/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.input

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.buttonSecondary
import zakadabar.stack.frontend.builtin.input.ZkCheckboxList
import zakadabar.stack.frontend.builtin.input.ZkCheckboxListItem
import zakadabar.stack.frontend.builtin.note.ZkNote
import zakadabar.stack.frontend.builtin.note.noteSecondary
import zakadabar.stack.frontend.builtin.note.noteSuccess
import zakadabar.stack.frontend.builtin.pages.zkPageStyles
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.util.marginRight

/**
 * This example shows how to create checkbox lists.
 */
class StringCheckboxListExample(
    element: HTMLElement
) : ZkElement(element) {


    class StringCheckboxList : ZkCheckboxList<String>() {
        override var items = listOf(
            ZkCheckboxListItem("value1", "First String", false),
            ZkCheckboxListItem("value2", "Second String", true),
            ZkCheckboxListItem("value3", "Third String", false)
        )
    }

    private val stringCheckboxList = StringCheckboxList()

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {
            + noteSuccess("Strings", stringCheckboxList) marginBottom 20
            + buttonSecondary(strings.execute, onClick = ::onSave) marginBottom 20
            + noteSecondary("Output", "")
        }
    }

    private fun onSave() {
        find<ZkNote>().first { it.flavour == ZkFlavour.Secondary }.content = zke {
            stringCheckboxList.items.forEach {
                + div { + "${it.value} = ${it.selected}" } marginRight 20
            }
        }
    }

}