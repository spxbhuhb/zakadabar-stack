/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.note

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.builtin.ZkElement

open class ZkNote(
    element: HTMLElement = document.createElement("div") as HTMLElement
) : ZkElement(element) {

    open val titleContainer = ZkElement()
    open val contentContainer = ZkElement()

    constructor(builder: ZkNote.() -> Unit) : this() {
        this.builder()
    }

    constructor(element: HTMLElement, builder: ZkNote.() -> Unit) : this(element) {
        this.builder()
    }

    var title: String? = null
        set(value) {
            field = value
            titleContainer.clear()
            field?.let {
                titleContainer.innerText = it
            }
        }

    var text: String? = null
        set(value) {
            field = value
            contentContainer.clear()
            field?.let { contentContainer.innerText = it }
        }

    var content: ZkElement? = null
        set(value) {
            field = value
            contentContainer.clear()
            field?.let { contentContainer += it }
        }

    override fun onCreate() {
        super.onCreate()
        + column {
            + titleContainer css ZkNoteStyles.title
            + contentContainer css ZkNoteStyles.content
        }
    }
}