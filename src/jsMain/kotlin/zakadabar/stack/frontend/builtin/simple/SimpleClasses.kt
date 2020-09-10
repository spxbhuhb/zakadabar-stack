/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.simple

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class SimpleClasses(theme: Theme) : CssStyleSheet<SimpleClasses>(theme) {

    companion object {
        var simpleClasses = SimpleClasses(FrontendContext.theme).attach()
    }

    val button by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        color = theme.darkestGray
        textTransform = "uppercase"
        cursor = "pointer"
        backgroundColor = theme.lightestColor
        padding = 10
        borderWidth = 1
        borderStyle = "solid"
        borderColor = theme.darkestGray
        borderRadius = 4
        on(":hover") {
            backgroundColor = theme.darkGray
            color = theme.lightestColor
        }
    }

}