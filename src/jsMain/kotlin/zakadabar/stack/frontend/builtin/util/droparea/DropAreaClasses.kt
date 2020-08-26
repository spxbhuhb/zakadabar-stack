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
package zakadabar.stack.frontend.builtin.util.droparea

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class DropAreaClasses(theme: Theme) : CssStyleSheet<DropAreaClasses>(theme) {

    companion object {
        var classes = DropAreaClasses(FrontendContext.theme).attach()
    }

    val dropArea by cssClass {
        flexGrow = 1
        width = "100%"
        display = "flex"
        flexDirection = "row"
        minHeight = 20
        justifyContent = "center"
        alignItems = "center"
        height = "100%"
        color = theme.darkestGray
        fill = theme.darkestGray
        marginTop = 20
        on(":hover") {
            backgroundColor = "#f5f5f5"
            borderRadius = 2
            border = "1px dotted lightgray"
        }
    }

    val dropAreaMessage by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 400
        paddingLeft = 6
    }

}