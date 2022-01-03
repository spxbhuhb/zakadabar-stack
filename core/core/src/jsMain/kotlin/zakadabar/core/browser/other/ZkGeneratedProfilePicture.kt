/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.other

import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.*
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

class ZkGeneratedProfilePicture(
    val name: String,
    val backGroundColor: String = ZkColors.white.alpha(0.5),
    val letterColor: String = theme.primaryColor
) : ZkElement() {

    override fun onCreate() {

        + JustifyContent.center
        + Display.flex
        + AlignItems.center

        + div {
            element.style.backgroundColor = backGroundColor
            element.style.borderRadius = "50%"
            element.style.width = "42px"
            element.style.height = "36px"
            + div {
                element.style.padding = "4px"
                element.style.fontWeight = "600"
                element.style.color = letterColor
                element.style.fontSize = "150%"
                element.style.textAlign = "center"
                + getLetters()
            }
        }
    }

    private fun getLetters(): String {
        var firstLetter = ""
        var secondLetter = ""
        if (name.isNotEmpty()) {
            val names = name.split("\\s+".toRegex())
            if (names.size >= 2) {
                firstLetter = names[0].substring(0, 1).uppercase()
                secondLetter = names[1].substring(0, 1).uppercase()
            } else {
                firstLetter = names[0].substring(0, 1).uppercase()
            }
        }
        return "$firstLetter$secondLetter"
    }
}