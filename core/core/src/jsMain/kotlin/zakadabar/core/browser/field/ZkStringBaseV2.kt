/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.util.plusAssign

abstract class ZkStringBaseV2<VT, FT : ZkStringBaseV2<VT,FT>>(
    context: ZkFieldContext,
    label: String,
    var getter: () -> String?,
    setter:(VT) -> Unit = { }
) : ZkFieldBase<VT, FT>(
    context = context,
    propName = label,
    label = label,
    setter = setter
) {

    open val input = document.createElement("input") as HTMLInputElement

    override var readOnly = context.readOnly
        set(value) {
            input.disabled = value
            field = value
        }

    open var submitOnEnter : Boolean = false

    abstract fun setBackingValue(value: String)

    override fun buildFieldValue() {

        if (readOnly) {
            input.readOnly = true
            input.classList += context.styles.disabledString
        } else {
            input.classList += context.styles.text
        }

        getter()?.let {
            input.value = it
        }

        on(input, "input") {
            setBackingValue(input.value)
        }

        on("keydown") { event ->
            event as KeyboardEvent
            if (submitOnEnter && event.key == "Enter") context.submit()
        }

        focusEvents(input)

        + input
    }

    override fun focusValue() {
        input.focus()
    }

}