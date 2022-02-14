/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.other

import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.*
import zakadabar.core.resource.css.*
import zakadabar.core.util.alpha

class ZkGeneratedProfilePicture(
    val name: String
) : ZkElement() {

    override fun onCreate() {

        + JustifyContent.center
        + Display.flex
        + AlignItems.center

        + div(zkOtherStyles.profContainer) {
            + div(zkOtherStyles.profLetters) {
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