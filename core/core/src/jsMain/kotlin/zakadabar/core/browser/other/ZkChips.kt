/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.core.browser.other

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.resource.*
import zakadabar.core.resource.css.AlignItems
import zakadabar.core.resource.css.Display
import zakadabar.core.resource.css.JustifyContent

class ZkChips(
    val name: String,
    val button: ZkButton? = null,
    val color: String = theme.primaryColor,
    val textColor: String = ZkColors.white
) : ZkElement() {

    override fun onCreate() {
        + JustifyContent.center

        + div {
            element.style.padding = "3px"
            + row {
                element.style.borderRadius = "24px"
                element.style.backgroundColor = color
                + Display.flex
                + AlignItems.center
                + div {
                    element.style.fontWeight = "400"
                    element.style.color = textColor
                    + name
                    element.style.paddingRight = button?.let {
                       "10px"
                    } ?: "20px"
                }
                element.style.paddingLeft = "20px"
                button?.let {
                    + it
                }
            }
        }
    }
}