/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.button

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import zakadabar.core.browser.application.application
import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.text.capitalized

/**
 * A simple clickable button with label and/or icon.
 *
 * @property  text          The text of the button, may be null when displaying only an icon.
 * @property  iconSource    The icon to show on the left, when null, no icon is shown.
 * @property  flavour       The flavor of the button, sets color set to use.
 * @property  url           The URL this button opens. This might be a local URL in which case
 *                          the default onClick calls [application.changeNavState]. When it is
 *                          not a local URL (starts with https:// or http://), the standard
 *                          browser mechanism for "a" tag is executed.
 * @property  round         When true and [text] is null, the icon button will be round. Not used
 *                          when [text] is not null.
 * @property  fill          When true, fills the button with the colour specified by [flavour].
 *                          When false, the button background is transparent.
 * @property  border        When true, adds border to the button using the flavour color.
 *                          When false, no border is added.
 * @property  capitalize    When true, capitalize the [text].
 * @property  tabIndex      When not null, add a tab index to this button. Default is 0.
 * @property  onClick       The function to execute when the button is clicked.
 */
open class ZkButton(
    open val text: String?,
    open val iconSource: ZkIconSource?,
    open val flavour: ZkFlavour = ZkFlavour.Primary,
    open val url: String? = null,
    open val round: Boolean = false,
    open val fill: Boolean = true,
    open val border: Boolean = true,
    open val capitalize: Boolean = true,
    open val iconSize: Int? = null,
    open val buttonSize: Int? = null,
    open val tabIndex: Int? = 0,
    open val onClick: (() -> Unit)? = null
) : ZkElement(document.createElement(if (url == null) "div" else "a") as HTMLElement)  {

    constructor(
        text: String,
        flavour: ZkFlavour = ZkFlavour.Primary,
        round: Boolean = false,
        fill: Boolean = true,
        border: Boolean = true,
        capitalize: Boolean = true,
        iconSize: Int? = null,
        buttonSize: Int? = null,
        tabIndex: Int? = 0,
        onClick: (() -> Unit)? = null
    ) : this(
        text, null, flavour, null, round, fill, border, capitalize, iconSize, buttonSize, tabIndex, onClick
    )

    constructor(
        text: String,
        flavour: ZkFlavour = ZkFlavour.Primary,
        url: String? = null,
        round: Boolean = false,
        fill: Boolean = true,
        border: Boolean = true,
        capitalize: Boolean = true,
        iconSize: Int? = null,
        buttonSize: Int? = null,
        tabIndex: Int? = 0,
        onClick: (() -> Unit)? = null
    ) : this(
        text, null, flavour, url, round, fill, border, capitalize, iconSize, buttonSize, tabIndex, onClick
    )

    constructor(
        iconSource: ZkIconSource,
        flavour: ZkFlavour = ZkFlavour.Primary,
        url: String? = null,
        round: Boolean = false,
        fill: Boolean = true,
        border: Boolean = true,
        capitalize: Boolean = true,
        iconSize: Int? = null,
        buttonSize: Int? = null,
        tabIndex: Int? = 0,
        onClick: (() -> Unit)? = null
    ) : this(
        null, iconSource, flavour, url, round, fill, border, capitalize, iconSize, buttonSize, tabIndex, onClick
    )

    open val localNav
        get() = url == null || (url?.startsWith("https://") != true && url?.startsWith("http://") != true)

    override fun onCreate() {

        setFlavourStyles()

        when {
            text == null -> buildIcon()
            iconSource == null -> buildText()
            else -> buildCombined()
        }

        if (element is HTMLAnchorElement) {
            element.href = url ?: ""
        }

        tabIndex?.let { element.tabIndex = it }

        on("click", ::onClick)
        on("mousedown", ::onMouseDown)
        on("keydown") { event ->
            event as KeyboardEvent
            if (event.key == "Enter") onEnter(event)
        }
    }

    open fun onEnter(event: Event) {
        if (localNav) {
            if (onClick != null) {
                onClick?.invoke()
            } else {
                url?.let { application.changeNavState(it) }
            }
        }
    }

    open fun onClick(event: Event) {
        if (localNav) {
            event.preventDefault()
            if (onClick != null) {
                onClick?.invoke()
            } else {
                url?.let { application.changeNavState(it) }
            }
        }
    }

    open fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

    open fun buildIcon() {
        classList += zkButtonStyles.icon
        classList += if (round) zkButtonStyles.round else zkButtonStyles.square

        buttonSize?.let {
            element.style.width = "${it}px"
            element.style.height = "${it}px"

            if (round) {
                element.style.borderRadius = "${it / 2}px"
            }
        }

        val svg = iconSource?.svg(iconSize ?: zkButtonStyles.iconSize) ?: throw IllegalStateException("missing icon source")

        innerHTML = svg

    }

    open fun buildText() {
        classList += zkButtonStyles.text

        + if (capitalize) text?.capitalized() else text
    }

    open fun buildCombined() {
        classList += zkButtonStyles.combined

        val svg = iconSource?.svg(zkButtonStyles.iconSize) ?: throw IllegalStateException("missing icon source")

        + div(zkButtonStyles.icon) {
            innerHTML = svg
        }

        + if (capitalize) text?.capitalized() else text
    }

    open fun setFlavourStyles() {
        when (flavour) {
            ZkFlavour.Primary -> {
                classList += if (fill) zkButtonStyles.primaryFill else zkButtonStyles.primaryNoFill
                classList += if (border) zkButtonStyles.primaryBorder else zkButtonStyles.primaryNoBorder
            }
            ZkFlavour.Secondary -> {
                classList += if (fill) zkButtonStyles.secondaryFill else zkButtonStyles.secondaryNoFill
                classList += if (border) zkButtonStyles.secondaryBorder else zkButtonStyles.secondaryNoBorder
            }
            ZkFlavour.Success -> {
                classList += if (fill) zkButtonStyles.successFill else zkButtonStyles.successNoFill
                classList += if (border) zkButtonStyles.successBorder else zkButtonStyles.successNoBorder
            }
            ZkFlavour.Warning -> {
                classList += if (fill) zkButtonStyles.warningFill else zkButtonStyles.warningNoFill
                classList += if (border) zkButtonStyles.warningBorder else zkButtonStyles.warningNoBorder
            }
            ZkFlavour.Danger -> {
                classList += if (fill) zkButtonStyles.dangerFill else zkButtonStyles.dangerNoFill
                classList += if (border) zkButtonStyles.dangerBorder else zkButtonStyles.dangerNoBorder
            }
            ZkFlavour.Info -> {
                classList += if (fill) zkButtonStyles.infoFill else zkButtonStyles.infoNoFill
                classList += if (border) zkButtonStyles.infoBorder else zkButtonStyles.infoNoBorder
            }
            ZkFlavour.Disabled -> {
                classList += if (fill) zkButtonStyles.disabledFill else zkButtonStyles.disabledNoFill
                classList += if (border) zkButtonStyles.disabledBorder else zkButtonStyles.disabledNoBorder
            }
            ZkFlavour.Custom -> Unit
        }
    }

}