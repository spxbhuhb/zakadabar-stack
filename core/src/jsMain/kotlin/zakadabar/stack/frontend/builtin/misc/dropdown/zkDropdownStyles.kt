/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.dropdown

import zakadabar.stack.frontend.resources.css.*

val zkDropdownStyles by cssStyleSheet(ZkDropdownStyles())

class ZkDropdownStyles : ZkCssStyleSheet() {

    val dropdown by cssClass {
        + Position.relative
    }

    val dropdownContent by cssClass {
        + Position.absolute
        + Display.none
        zIndex = 100.zIndex
        outline = "none"
    }

    val dropdownActive by cssClass {
        display = "initial"
    }
}