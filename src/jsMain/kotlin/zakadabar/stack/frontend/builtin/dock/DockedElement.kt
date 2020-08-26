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
@file:Suppress("MemberVisibilityCanBePrivate")

package zakadabar.stack.frontend.builtin.dock

import kotlinx.browser.document
import org.w3c.dom.HTMLHtmlElement
import zakadabar.stack.frontend.FrontendContext.dock
import zakadabar.stack.frontend.FrontendContext.theme
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.header.Header
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.SimpleElement

/**
 * An element to put into the dock with a header, minimize, normal, maximize, close actions.
 * You may extend this class or just pass an element in the content field.
 *
 * If you extend you have to modify the [update] method to show/hide whatever is docked.
 */
open class DockedElement(
    val icon: SimpleElement,
    val title: String,
    var state: DockedElementState,
    var content: SimpleElement? = null,
) : ComplexElement() {

    protected val minimize = Icons.minimize.complex16(::onMinimize)
    protected val maximize = Icons.maximize.complex16(::onMaximize)
    protected val openInFull = Icons.openInFull.complex16(::onOpenInFullIcon)
    protected val closeFullScreen = Icons.closeFullScreen.complex16(::onCloseFullScreen)
    protected val close = Icons.close.complex16(::onClose)

    protected val header = Header(title, icon, tools = listOf(minimize, maximize, openInFull, closeFullScreen, close))

    override fun init(): DockedElement {
        className += coreClasses.dockItem

        this += header
        this += content

        update(state)

        return this
    }

    open fun update(newState: DockedElementState) {
        state = newState

        setSize()

        when (state) {
            DockedElementState.Minimized -> {
                minimize.hide()
                maximize.show()
                openInFull.show()
                closeFullScreen.hide()

                content?.hide()
            }
            DockedElementState.Normal -> {
                minimize.show()
                maximize.hide()
                openInFull.show()
                closeFullScreen.hide()

                content?.show()
            }
            DockedElementState.Maximized -> {
                minimize.show()
                maximize.hide()
                openInFull.hide()
                closeFullScreen.show()

                content?.show()
            }
        }
    }

    open fun setSize() {
        val html = document.documentElement as HTMLHtmlElement
        val htmlHeight = html.clientHeight
        val htmlWidth = html.clientWidth

        when (state) {
            DockedElementState.Minimized -> {
                with(element.style) {
                    width = "max-content"
                    height = "${theme.headerHeight}px"
                    margin = "0px"
                }
            }

            DockedElementState.Normal -> {
                // FIXME this is most probably wrong
                with(element.style) {
                    width = "600px"
                    height = "400px"
                    margin = "10px"
                }
            }

            DockedElementState.Maximized -> {
                with(element.style) {
                    width = "${htmlWidth - 20}px"
                    height = "${htmlHeight - 20}px"
                    margin = "10px"
                }
            }
        }
    }

    open fun onMinimize() {
        update(DockedElementState.Minimized)
    }

    open fun onMaximize() {
        update(DockedElementState.Normal)
    }

    open fun onOpenInFullIcon() {
        update(DockedElementState.Maximized)
    }

    open fun onCloseFullScreen() {
        update(DockedElementState.Normal)
    }

    open fun onClose() {
        dock -= this
    }

}