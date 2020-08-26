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
package zakadabar.stack.frontend.builtin.input

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class InputClasses(theme: Theme) : CssStyleSheet<InputClasses>(theme) {

    companion object {
        val inputClasses = InputClasses(FrontendContext.theme).attach()
    }

    val inputContentElement by cssClass {
        boxSizing = "border-box"
        height = "100%"
        width = "100%"
        overflow = "hidden"
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        borderRadius = 2
        backgroundColor = theme.headerBackground
    }

    val inputPrefixIcon by cssClass {
        boxSizing = "border-box"
        backgroundColor = theme.headerForeground
        borderTopLeftRadius = 2
        borderBottomLeftRadius = 2
        padding = 4
        height = 26
        fill = theme.lightestColor
    }

    val inputInput by cssClass {
        boxSizing = "border-box"
        flexGrow = 1
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.headerForeground
        fontFamily = "'Open Sans', sans-serif"
        fontSize = 12
        height = 26
        padding = 4
        margin = 0
        borderLeft = 0
        borderRight = 0
        borderTop = theme.darkestColor
        borderBottom = theme.darkestColor
        outline = "none"
    }

    val inputPostfix by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-end"
        alignItems = "center"
        height = 26
    }

    val approveFill by cssClass {
        fill = theme.lightestColor
        backgroundColor = theme.darkColor
    }

    val cancelFill by cssClass {
        fill = theme.darkGray
        backgroundColor = theme.lightGray
        border = "1px solid " + theme.darkColor
        borderBottomRightRadius = 2
        borderTopRightRadius = 2
    }

    val inputPostfixIcon by cssClass {
        boxSizing = "border-box"
        padding = "4px"
        height = 26
        on(":last-child") {
            borderBottomRightRadius = 2
            borderTopRightRadius = 2
        }
    }

}