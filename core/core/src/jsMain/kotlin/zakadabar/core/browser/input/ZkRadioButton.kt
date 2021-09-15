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

open class ZkRadioButton(
    open val iconSource: ZkIconSource = ZkIcons.circle,
    val group : String,
    val text : String,
    checked: Boolean = false,
    readOnly: Boolean = false,
    val onChange: ((Boolean) -> Unit)? = null
) : ZkElement() {

    open val radio = document.createElement("input") as HTMLInputElement
    open val label = document.createElement("label") as HTMLLabelElement

    open var readOnly: Boolean = readOnly
        set(value) {
            radio.readOnly = value
            radio.disabled = value
            field = value
        }

    open var disabled: Boolean
        get() = radio.disabled
        set(value) {
            radio.disabled = value
        }

    open var checked: Boolean = checked
        set(value) {
            if (radio.checked != value) radio.checked = value
            field = value
        }

    override fun onCreate() {
        buildPoint.tabIndex = 0

        + zkInputStyles.radioOuter

        + div(zkInputStyles.radioControlContainer) {

            radio.id = "${this.id}-radio"
            radio.type = "radio"
            radio.name = group
            radio.classList += zkInputStyles.radioNative
            radio.checked = checked
            radio.readOnly = readOnly
            radio.disabled = readOnly
            + radio

            label.htmlFor = radio.id
            label.classList += zkInputStyles.radioLabel
            + label

            label.innerHTML = iconSource.svg(13)
        }

        + div(zkInputStyles.radioText) {
            + text
        }

        on(radio, "click", ::onChange)

        on(buildPoint, "keypress") { event ->
            event as KeyboardEvent
            when (event.key) {
                "Enter", " " -> {
                    radio.checked = ! radio.checked
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

        checked = radio.checked
        onChange?.invoke(radio.checked)
    }

}