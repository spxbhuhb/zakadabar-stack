/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.util.PublicApi

/**
 * A simple input text field.
 *
 * @property  enter     When true [onChange] is called only when the user hits enter.
 * @property  value     Initial value of the field.
 * @property  onChange  The function to execute when the value of the field changes.
 *
 * @since  2021.1.14
 */
@PublicApi
open class ZkTextInput(
    private val enter: Boolean = false,
    value: String = "",
    private val onChange: (String) -> Unit = { }
) : ZkElement(
    element = document.createElement("input") as HTMLInputElement
) {

    val input = element as HTMLInputElement

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
            if (! enter) onChange(value)
        }

        on("keydown") { event ->
            event as KeyboardEvent
            if (enter && event.key == "Enter") onChange(value)
        }

    }

    override fun focus() : ZkElement {
        input.focus()
        return this
    }

    override fun toString() = value

}