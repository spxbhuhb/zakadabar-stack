/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.input

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLLabelElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.ZkIcons

open class ZkCheckBox(
    open val iconSource: ZkIconSource = ZkIcons.check,
    checked: Boolean = false,
    readOnly: Boolean = false,
    val onChange: ((Boolean) -> Unit)? = null
) : ZkElement() {

    open val checkbox = document.createElement("input") as HTMLInputElement
    open val label = document.createElement("label") as HTMLLabelElement

    open var readOnly: Boolean = readOnly
        set(value) {
            checkbox.readOnly = value
            checkbox.disabled = value
            field = value
        }

    open var disabled: Boolean
        get() = checkbox.disabled
        set(value) {
            checkbox.disabled = value
        }

    open var checked: Boolean = checked
        set(value) {
            if (checkbox.checked != value) checkbox.checked = value
            field = value
        }

    override fun onCreate() {
        buildPoint.tabIndex = 0

        classList += zkInputStyles.checkBoxOuter

        checkbox.id = "${this.id}-checkbox"
        checkbox.type = "checkbox"
        checkbox.classList += zkInputStyles.checkBoxNative
        checkbox.checked = checked
        checkbox.readOnly = readOnly
        checkbox.disabled = readOnly
        + checkbox

        label.htmlFor = checkbox.id
        label.classList += zkInputStyles.checkboxLabel
        + label

        label.innerHTML = iconSource.svg(18)

        on(checkbox, "click", ::onChange)

        on(buildPoint, "keypress") { event ->
            event as KeyboardEvent
            when (event.key) {
                "Enter", " " -> {
                    checkbox.checked = ! checkbox.checked
                    onChange(event)
                    event.preventDefault()
                }
            }
        }

    }

    open fun onChange(event: Event) {
        if (readOnly) {
            event.preventDefault()
            return
        }

        checked = checkbox.checked
        onChange?.invoke(checkbox.checked)
    }

}