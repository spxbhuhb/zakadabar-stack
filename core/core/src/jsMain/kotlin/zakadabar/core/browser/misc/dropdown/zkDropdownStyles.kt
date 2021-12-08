/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.misc.dropdown

import zakadabar.core.resource.css.*

var zkDropdownStyles by cssStyleSheet(ZkDropdownStyles())

open class ZkDropdownStyles : ZkCssStyleSheet() {

    open val dropdown by cssClass {
        + Position.relative
    }

    open val dropdownContent by cssClass {
        + Position.absolute
        + Display.none
        zIndex = 2000.zIndex
        outline = "none"
    }

    open val dropdownActive by cssClass {
        display = "initial"
    }
}