/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.dropdown

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

class DropdownClasses(theme: ZkTheme) : CssStyleSheet<DropdownClasses>(theme) {

    companion object {
        var dropdownClasses = DropdownClasses(Application.theme).attach()
    }

    val dropdown by cssClass {
        position = "relative"
    }

    val dropdownContent by cssClass {
        position = "absolute"
        display = "none"
        zIndex = 100
    }

    val dropdownActive by cssClass {
        display = "initial"
    }
}