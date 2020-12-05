/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class MenuClasses(theme: Theme) : CssStyleSheet<MenuClasses>(theme) {

    companion object {
        var menuClasses = MenuClasses(Application.theme).attach()
    }

    val item by cssClass {
        color = theme.activeBlue
        cursor = "pointer"
        padding = 10
        on(":hover") {
            backgroundColor = theme.darkGray
            color = theme.lightestColor
        }
    }

    val groupTitle by cssClass {
        color = theme.activeBlue
        cursor = "pointer"
        padding = 10
        on(":hover") {
            backgroundColor = theme.darkGray
            color = theme.lightestColor
        }
    }

    val groupContent by cssClass {
        paddingLeft = 20
    }

}