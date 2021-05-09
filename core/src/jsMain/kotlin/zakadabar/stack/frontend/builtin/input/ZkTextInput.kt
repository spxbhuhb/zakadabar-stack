/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.util.PublicApi

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
    var value: String = "",
    private val onChange: (String) -> Unit = { }
) : ZkElement(
    element = document.createElement("input") as HTMLInputElement
) {

    private val input = element as HTMLInputElement

    override fun onCreate() {

        className = zkInputStyles.standaloneInput

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

}