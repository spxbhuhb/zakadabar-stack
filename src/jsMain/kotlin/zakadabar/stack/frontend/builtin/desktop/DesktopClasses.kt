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
package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext.theme
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class DesktopClasses(theme: Theme) : CssStyleSheet<DesktopClasses>(theme) {

    companion object {
        val desktopClasses = DesktopClasses(theme).attach()
    }

    val desktop by cssClass {
        boxSizing = "border-box"

        width = "100%"
        height = "100%"

        display = "flex"
        flexDirection = "column"

        borderRadius = 2
        backgroundColor = it.headerBackground
    }

    val header by cssClass {
        boxSizing = "border-box"

        display = "flex"
        flexDirection = "row"

        height = 26
        alignItems = "center"
        backgroundColor = "rgba(13,91,40,0.2)"
    }

    val headerIcon by cssClass {
        boxSizing = "border-box"
        backgroundColor = it.headerForeground
        padding = 6
        height = 26
        width = 26
        fill = it.lightestColor
    }

    val center by cssClass {
        flexGrow = 1
        display = "flex"
        flexDirection = "row"
    }

    val main by cssClass {
        flexGrow = 1
    }

    val footer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        boxSizing = "border-box"
        height = 24
        fontFamily = it.fontFamily
        fontSize = it.fontSize
        borderRadius = it.borderRadius
        backgroundColor = it.lightGray
    }

    val copyright by cssClass {
        paddingLeft = 16
        color = it.darkestGray
    }

}