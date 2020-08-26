/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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