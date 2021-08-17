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
class IntCheckboxListExample(
    element: HTMLElement
) : ZkElement(element) {

    private var intItems = listOf(
        ZkCheckboxListItem(1, "First Integer", true),
        ZkCheckboxListItem(2, "Second Integer", true),
        ZkCheckboxListItem(5, "Fifth Integer", false)
    )

    private val intCheckboxList = ZkCheckboxList(intItems)

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {
            + noteSuccess("Integers", intCheckboxList) marginBottom 20
            + buttonSecondary(localizedStrings.execute, onClick = ::onExecute) marginBottom 20
            + noteSecondary("Output", "")
        }
    }

    private fun onExecute() {
        find<ZkNote>().first { it.flavour == ZkFlavour.Secondary }.content = zke {
            intCheckboxList.items.forEach {
                + div { + "${it.value} = ${it.selected}" } marginRight 20
            }
        }
    }

}