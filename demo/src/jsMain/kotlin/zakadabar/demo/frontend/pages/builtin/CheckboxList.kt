/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.builtin

import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.input.checkboxlist.ZkCheckboxList
import zakadabar.stack.frontend.builtin.input.checkboxlist.ZkCheckboxListItem
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.marginRight

/**
 * This example shows how to create checkbox lists.
 */
object CheckboxList : ZkPage(
    title = "ZkCheckboxList"
) {

    var intItems = listOf(
        ZkCheckboxListItem(1, "label1", true),
        ZkCheckboxListItem(2, "label2", true),
        ZkCheckboxListItem(3, "label3", false)
    )

    val strings = StringCheckboxList()
    val ints = ZkCheckboxList(intItems)

    val output = ZkElement()

    override fun onCreate() {
        super.onCreate()
        + column {
            + "Checkbox list with String values."
            + strings marginBottom 10
            + "Checkbox list with int values"
            + ints

            + row {
                + ZkButton(builtin.save, ::onSave)
            }

            + output
        }
    }

    class StringCheckboxList : ZkCheckboxList<String>() {
        override var items = listOf(
            ZkCheckboxListItem("value1", "label1", false),
            ZkCheckboxListItem("value2", "label2", true),
            ZkCheckboxListItem("value3", "label3", false)
        )
    }

    private fun onSave() {
        output.clear()

        output.build {
            + "Strings"
            strings.items.forEach {
                + div { + "${it.value} = ${it.selected}" } marginRight 20
            }

            + "Ints"
            ints.items.forEach {
                + div { + "${it.value} = ${it.selected}" } marginRight 20
            }
        }
    }

}