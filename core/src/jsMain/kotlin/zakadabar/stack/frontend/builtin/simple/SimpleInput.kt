/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.simple

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.util.PublicApi

/**
 * A simple input text field.
 *
 * @property  onChange  The function to execute when the value of the field changes.
 *
 * @since  2021.1.14
 */
@PublicApi
class SimpleInput(
    private val onChange: (String) -> Unit
) : ZkElement(
    element = document.createElement("input") as HTMLInputElement
) {

    private val input = element as HTMLInputElement

    override fun init(): ZkElement {

        on("input") { _ ->
            onChange(input.value)
        }

        return this
    }

}