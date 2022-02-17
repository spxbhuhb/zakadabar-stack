/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.core.browser.other

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.resource.css.JustifyContent

class ZkChips(
    val name: String,
    val button: ZkButton? = null
) : ZkElement() {

    override fun onCreate() {
        + JustifyContent.center

        + div {
            + row(zkOtherStyles.chipsContainer) {
                + div(zkOtherStyles.chipsLetters) {
                    + name
                    style {
                        paddingRight = button?.let {
                            "10px"
                        } ?: "20px"
                    }
                }
                button?.let {
                    + it
                }
            }
        }
    }
}