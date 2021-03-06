/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.dropdown

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

class ZkDropdownClasses(theme: ZkTheme) : CssStyleSheet<ZkDropdownClasses>(theme) {

    companion object {
        var dropdownClasses = ZkDropdownClasses(Application.theme).attach()
    }

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