/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package zakadabar.stack.frontend.builtin.dock

import kotlinx.browser.document
import org.w3c.dom.HTMLHtmlElement
import zakadabar.stack.frontend.FrontendContext.dock
import zakadabar.stack.frontend.FrontendContext.theme
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.header.Header
import zakadabar.stack.frontend.builtin.util.header.HeaderClasses.Companion.headerClasses
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement

/**
 * An element to put into the dock with a header, minimize, normal, maximize, close actions.
 * You may extend this class or just pass an element in the content field.
 *
 * If you extend you have to modify the [update] method to show/hide whatever is docked.
 */
open class DockedElement(
    val icon: ZkElement,
    val title: String,
    var state: DockedElementState,
    var content: ZkElement? = null,
) : ZkElement() {

    protected val minimize = Icons.minimize.complex16(::onMinimize).withClass(headerClasses.extensionIcon)
    protected val maximize = Icons.maximize.complex16(::onMaximize).withClass(headerClasses.extensionIcon)
    protected val openInFull = Icons.openInFull.complex16(::onOpenInFullIcon).withClass(headerClasses.extensionIcon)
    protected val closeFullScreen =
        Icons.closeFullScreen.complex16(::onCloseFullScreen).withClass(headerClasses.extensionIcon)
    protected val close = Icons.close.complex16(::onClose).withClass(headerClasses.extensionIcon)

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