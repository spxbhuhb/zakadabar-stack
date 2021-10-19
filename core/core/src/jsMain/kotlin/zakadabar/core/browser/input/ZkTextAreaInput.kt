/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.input

import kotlinx.browser.document
import org.w3c.dom.HTMLTextAreaElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.util.PublicApi

/**
 * A simple input text field.
 *
 * @property  value     Initial value of the field.
 * @property  onChange  The function to execute when the value of the field changes.
 */
@PublicApi
open class ZkTextAreaInput(
    value: String = "",
    val onChange: (String) -> Unit = { }
) : ZkElement(
    element = document.createElement("textarea") as HTMLTextAreaElement
) {

    val input = element as HTMLTextAreaElement

    open var value: String = value
        set(value) {
            if (input.value != value) input.value = value
            field = value
        }

    override fun onCreate() {

        + zkInputStyles.textInput

        input.value = value

        on("input") {
            value = input.value
        }

    }

    override fun focus() : ZkElement {
        input.focus()
        return this
    }

    override fun toString() = value

}