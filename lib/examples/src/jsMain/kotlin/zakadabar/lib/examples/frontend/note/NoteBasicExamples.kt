/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.note

import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.note.*
import zakadabar.stack.frontend.builtin.pages.zkPageStyles

/**
 * This example shows how to create basic notes.
 */
class NoteBasicExamples(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + column(zkPageStyles.content) {

            + primaryNote("Primary Note", "Content of the note.") marginBottom 20
            + secondaryNote("Secondary Note", "Content of the note.") marginBottom 20
            + successNote("Success Note", "Content of the note.") marginBottom 20
            + warningNote("Warning Note", "Content of the note.") marginBottom 20
            + dangerNote("Danger Note", "Content of the note.") marginBottom 20
            + infoNote("Info Note", "Content of the note.") marginBottom 20

        }
    }

}