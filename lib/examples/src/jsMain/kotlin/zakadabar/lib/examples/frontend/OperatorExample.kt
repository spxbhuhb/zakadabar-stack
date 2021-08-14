/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.button.buttonSuccess
import zakadabar.core.frontend.builtin.layout.zkLayoutStyles
import zakadabar.core.frontend.builtin.note.ZkNote
import zakadabar.core.frontend.builtin.note.noteSuccess
import zakadabar.core.frontend.util.marginBottom

class OperatorExample(
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

            container += buttonSuccess("A Button, click to hide the note") {
                container -= ZkNote::class
            } marginBottom 20

            container += noteSuccess("A Note", "This note will disappear shortly.")
        }
    }
}