/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.dropdown

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkDropdownStyles by cssStyleSheet(ZkDropdownStyles())

class ZkDropdownStyles : ZkCssStyleSheet<ZkTheme>() {

    val dropdown by cssClass {
        position = "relative"
    }

    val dropdownContent by cssClass {
        position = "absolute"
        display = "none"
        zIndex = 100
        outline = "none"
    }

    val dropdownActive by cssClass {
        display = "initial"
    }
}