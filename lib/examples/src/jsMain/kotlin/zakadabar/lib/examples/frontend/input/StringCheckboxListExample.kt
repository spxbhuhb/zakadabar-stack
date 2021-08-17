/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.input

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonSecondary
import zakadabar.core.browser.input.ZkCheckboxList
import zakadabar.core.browser.input.ZkCheckboxListItem
import zakadabar.core.browser.note.ZkNote
import zakadabar.core.browser.note.noteSecondary
import zakadabar.core.browser.note.noteSuccess
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.browser.util.marginRight
import zakadabar.core.resource.localizedStrings

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
            + buttonSecondary(localizedStrings.execute, onClick = ::onSave) marginBottom 20
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