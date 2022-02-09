/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.field

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.util.plusAssign

/**
 * @property  context   The context that contains this field.
 * @property  propName  Name of the class property this field displays, null when there is no backing property.
 * @property  label     Label of the field. When null, and [propName] is not null, the localized [propName] is used as label.
 * @property  getter    Function used to initialize the value of the field.
 * @property  setter    Function to call when value of the field changes and it should be written back to somewhere.
 */
abstract class ZkStringBaseV2<VT, FT : ZkStringBaseV2<VT,FT>>(
    context: ZkFieldContext,
    propName: String? = null,
    label: String? = null,
    var getter: () -> String?,
    setter:(VT) -> Unit = { }
) : ZkFieldBase<VT, FT>(
    context = context,
    propName = propName,
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