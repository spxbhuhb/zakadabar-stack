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

package zakadabar.stack.frontend.builtin.navigator

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class NavigatorClasses(theme: Theme) : CssStyleSheet<NavigatorClasses>(theme) {

    companion object {
        val navigatorClasses = NavigatorClasses(FrontendContext.theme).attach()
    }

    val navigator by cssClass {
        width = 300
        minWidth = 300
        height = "100%"
        display = "flex"
        flexDirection = "column"
    }

    val content by cssClass {
        flexGrow = 1
        width = "100%"
        boxSizing = "border-box"
        padding = 8
        display = "flex"
        flexDirection = "column"
        justifyContent = "start"
        alignItems = "start"
        overflowY = "scroll"
        overflowX = "hidden"
    }

    val items by cssClass {
        width = "100%"
    }

    val entityListItem by cssClass {
        boxSizing = "border-box"
        display = "flex"
        flexDirection = "row"
        alignItems = "baseline"
        padding = 2
        width = "100%"
        borderRadius = 2
        fontFamily = theme.fontFamily
        fontSize = 12
        cursor = "pointer"
        userSelect = "none"
        on(":hover") {
            backgroundColor = theme.gray
        }
    }

    val selectedListItem by cssClass {
        backgroundColor = theme.selectedColor + " !important"
        color = theme.lightestColor
    }

    val entityListIcon by cssClass {
        verticalAlign = "text-bottom"
        position = "relative"
        // these two attributes really depend on the icon
        // for 18px sized Glypicons top 2 is the proper value
        fill = theme.darkGray
        top = 2.5
    }

    val entityListName by cssClass {
        paddingLeft = 4
    }

    val entityListDetail by cssClass {
        paddingLeft = 4
        fontSize = "90%"
        color = theme.darkGray
    }

    val entityPreviewContent by cssClass {
        boxSizing = "border-box"
        border = "1px solid lightgray"
        borderRadius = "2px"
        width = "100%"
        height = "100%"
        padding = 6
        backgroundColor = theme.lightGray
    }

    val noPreview by cssClass {
        display = "flex"
        flexDirection = "column"
        justifyContent = "center"
        alignItems = "center"
        height = "100%"
        color = theme.darkestGray
    }

    val noPreviewInstructions by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 400
    }

    val newEntity by cssClass {
        boxSizing = "border-box"
        width = "calc(100% - 20px)"
        margin = 10
        border = "1px solid" + theme.darkColor
    }

    val newEntityItem by cssClass {
        boxSizing = "border-box"
        display = "flex"
        flexDirection = "row"
        alignItems = "baseline"
        height = 24
        padding = "0px 4px 2px 4px"
        width = "100%"
        fontFamily = theme.fontFamily
        fontSize = 12
        cursor = "pointer"
        userSelect = "none"
        on(":hover") {
            backgroundColor = theme.gray
        }
    }

    val newEntityIcon by cssClass {
        verticalAlign = "text-bottom"
        position = "relative"
        // these two attributes really depend on the icon
        // for 18px sized Glypicons top 2 is the proper value
        fill = theme.darkColor
        top = 2.5
    }

    val newEntityName by cssClass {
        paddingLeft = 4
    }

    val newEntityActionIcon by cssClass {
        boxSizing = "border-box"
        padding = "4px 4px"
        height = 26
        width = 26
        fill = theme.darkColor
        backgroundColor = theme.lightGray
        borderTop = "1px solid" + theme.darkColor
        borderBottom = "1px solid" + theme.darkColor
        strokeWidth = 2
        cursor = "pointer"

        on(":first-child") {
            paddingLeft = 7
        }

        on(":last-child") {
            paddingRight = 6
        }
    }

}