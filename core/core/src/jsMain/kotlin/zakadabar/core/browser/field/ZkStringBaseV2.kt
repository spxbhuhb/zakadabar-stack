/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import zakadabar.core.browser.field.ZkFieldBase
import zakadabar.core.browser.field.ZkFieldContext
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.util.plusAssign

abstract class ZkStringBaseV2<VT, FT : ZkStringBaseV2<VT,FT>>(
    context: ZkFieldContext,
    private val title: String,
    val getter: () -> String?
) : ZkFieldBase<VT, FT>(
    context = context,
    propName = title,
    label = title
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
            input.value = it // todo: ask Zoli if its ok like this..
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