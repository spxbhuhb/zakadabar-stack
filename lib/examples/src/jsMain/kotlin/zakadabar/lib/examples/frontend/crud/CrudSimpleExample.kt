/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import org.w3c.dom.HTMLElement
import zakadabar.lib.examples.data.SimpleExampleAction
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.data.SimpleExampleQuery
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.buttonPrimary
import zakadabar.core.browser.crud.ZkInlineCrud
import zakadabar.core.browser.input.ZkTextInput
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.note.noteSecondary
import zakadabar.core.resource.css.px
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.marginBottom

class SimpleExampleInlineCrud : ZkInlineCrud<SimpleExampleBo>() {
    init {
        companion = SimpleExampleBo.Companion
        boClass = SimpleExampleBo::class
        editorClass = SimpleExampleForm::class
        tableClass = SimpleExampleTable::class
    }
}

class CrudSimpleExample(
    element: HTMLElement
) : ZkElement(element) {

    private val output = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + div {
            + "This demo accepts  maximum 1000 entities. After that you'll get an error."
        } marginBottom 10

        + div {
            height = 400.px
            + zkLayoutStyles.fixBorder
            + SimpleExampleInlineCrud().apply { openAll() }
        } marginBottom 10

        + row(grid = true) {
            gridTemplateColumns = "repeat(4, max-content)"

            + buttonPrimary("Action") {
                io {
                    output.clear()
                    val result = SimpleExampleAction(name = "hello").execute()
                    output build { + div { + "action result: ${result.success}" } }
                }
            }

            + div(zkLayoutStyles.alignSelfCenter) { + "Name:" }
            + ZkTextInput()

            + buttonPrimary("Query") {
                io {
                    output.clear()
                    SimpleExampleQuery(name = first<ZkTextInput>().value).execute().forEach {
                        output build { + div { + it.name2x } }
                    }
                }
            }
        } marginBottom 10

        + noteSecondary("Output", output)
    }
}