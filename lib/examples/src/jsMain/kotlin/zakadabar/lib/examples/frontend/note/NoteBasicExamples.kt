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

            + notePrimary("Primary Note", "Content of the note.") marginBottom 20
            + noteSecondary("Secondary Note", "Content of the note.") marginBottom 20
            + noteSuccess("Success Note", "Content of the note.") marginBottom 20
            + noteWarning("Warning Note", "Content of the note.") marginBottom 20
            + noteDanger("Danger Note", "Content of the note.") marginBottom 20
            + noteInfo("Info Note", "Content of the note.") marginBottom 20

        }
    }

}