/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.core.browser.other

import org.w3c.dom.events.Event
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIconSource

/**
 * A simple chip with an optional icon button.
 *
 * @property  text          The text of the chip.
 * @property  icon    The icon for the button, optional.
 * @property  flavour       The flavor of the button, sets color set to use.
 * @property  capitalize    When true, capitalize the [text].
 * @property  tabIndex      When not null, add a tab index to this button. Default is 0.
 * @property  onCancel       The function to execute when the icon button is clicked.
 */
open class ZkChip(
    open val text: String?,
    val icon: ZkIconSource? = null,
    open val flavour: ZkFlavour = ZkFlavour.Primary,
    open val fill: Boolean = true,
    open val border: Boolean = true,
    val cancelIcon: ZkIconSource? = null,
    open val capitalize: Boolean = true,
    open val tabIndex: Int? = 0,
    open val onCancel: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {

        + zkChipStyles.container

        setFlavourStyles()

        if (icon == null) {
            + zkChipStyles.textLeftPaddingNoIcon
        } else {
            + ZkIcon(icon, size = zkChipStyles.iconSize) css zkChipStyles.icon
        }

        + text

        if (cancelIcon == null) {
            + zkChipStyles.textRightPaddingNoCancel
        } else {
            + ZkIcon(cancelIcon, size = zkChipStyles.cancelIconSize).apply {
                + zkChipStyles.cancelIcon
                on("click", ::onClick)
                on("mousedown", ::onMouseDown)
            }
        }
    }

    open fun onClick(event: Event) {
        event.preventDefault()
        onCancel?.invoke()
    }

    open fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

    open fun setFlavourStyles() {
        when (flavour) {
            ZkFlavour.Primary -> {
                classList += if (fill) zkChipStyles.primaryFill else zkChipStyles.primaryNoFill
                classList += if (border) zkChipStyles.primaryBorder else zkChipStyles.primaryNoBorder
            }
            ZkFlavour.Secondary -> {
                classList += if (fill) zkChipStyles.secondaryFill else zkChipStyles.secondaryNoFill
                classList += if (border) zkChipStyles.secondaryBorder else zkChipStyles.secondaryNoBorder
            }
            ZkFlavour.Success -> {
                classList += if (fill) zkChipStyles.successFill else zkChipStyles.successNoFill
                classList += if (border) zkChipStyles.successBorder else zkChipStyles.successNoBorder
            }
            ZkFlavour.Warning -> {
                classList += if (fill) zkChipStyles.warningFill else zkChipStyles.warningNoFill
                classList += if (border) zkChipStyles.warningBorder else zkChipStyles.warningNoBorder
            }
            ZkFlavour.Danger -> {
                classList += if (fill) zkChipStyles.dangerFill else zkChipStyles.dangerNoFill
                classList += if (border) zkChipStyles.dangerBorder else zkChipStyles.dangerNoBorder
            }
            ZkFlavour.Info -> {
                classList += if (fill) zkChipStyles.infoFill else zkChipStyles.infoNoFill
                classList += if (border) zkChipStyles.infoBorder else zkChipStyles.infoNoBorder
            }
            ZkFlavour.Disabled -> {
                classList += if (fill) zkChipStyles.disabledFill else zkChipStyles.disabledNoFill
                classList += if (border) zkChipStyles.disabledBorder else zkChipStyles.disabledNoBorder
            }
            ZkFlavour.Custom -> Unit
        }
    }

}