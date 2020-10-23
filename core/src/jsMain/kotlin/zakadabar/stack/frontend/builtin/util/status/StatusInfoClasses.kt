/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.status

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class StatusInfoClasses(theme: Theme) : CssStyleSheet<StatusInfoClasses>(theme) {

    companion object {
        var classes = StatusInfoClasses(FrontendContext.theme).attach()
    }

    val loading by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.darkestGray
        fill = theme.darkestGray
    }

    val loadingMessage by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        paddingLeft = 6
    }

    val empty by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.infoColor
        fill = theme.infoColor
    }

    val emptyMessage by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        paddingLeft = 6
    }

    val error by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.errorColor
        fill = theme.errorColor
    }

    val errorMessage by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        paddingLeft = 6
    }

}