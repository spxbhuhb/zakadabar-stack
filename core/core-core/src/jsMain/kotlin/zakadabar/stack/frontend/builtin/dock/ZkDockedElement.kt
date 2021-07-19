/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("MemberVisibilityCanBePrivate")

package zakadabar.stack.frontend.builtin.dock

import kotlinx.browser.document
import org.w3c.dom.HTMLHtmlElement
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.resources.ZkIconSource
import zakadabar.stack.frontend.resources.ZkIcons

/**
 * An element to put into the dock with a header, minimize, normal, maximize, close actions.
 * You may extend this class or just pass an element in the content field.
 *
 * If you extend you have to modify the [update] method to show/hide whatever is docked.
 */
open class ZkDockedElement(
    val icon: ZkIconSource,
    val title: String,
    var state: ZkDockedElementState,
    var content: ZkElement? = null,
) : ZkElement() {

    protected val headerIcon = ZkIcon(icon)
    protected val minimize = ZkIcon(ZkIcons.minimize, 16, onClick = ::onMinimize).css(zkDockStyles.extensionIcon)
    protected val maximize = ZkIcon(ZkIcons.maximize, 16, onClick = ::onMaximize).css(zkDockStyles.extensionIcon)
    protected val openInFull = ZkIcon(ZkIcons.openInFull, 16, onClick = ::onOpenInFullIcon).css(zkDockStyles.extensionIcon)
    protected val closeFullScreen = ZkIcon(ZkIcons.closeFullScreen, 16, onClick = ::onCloseFullScreen).css(zkDockStyles.extensionIcon)
    protected val close = ZkIcon(ZkIcons.close, 16, onClick = ::onClose).css(zkDockStyles.extensionIcon)

    protected val header = ZkDockedElementHeader(title, headerIcon, tools = listOf(minimize, maximize, openInFull, closeFullScreen, close))

    override fun onCreate() {
        super.onCreate()

        className += zkDockStyles.dockItem

        this += header
        this += content

        update(state)
    }

    /**
     * Add the element to the application dock.
     */
    open fun run() : ZkDockedElement {
        application.dock += this
        return this
    }

    open fun update(newState: ZkDockedElementState) {
        state = newState

        setSize()

        when (state) {
            ZkDockedElementState.Minimized -> {
                minimize.hide()
                maximize.show()
                openInFull.show()
                closeFullScreen.hide()

                content?.hide()
            }
            ZkDockedElementState.Normal -> {
                minimize.show()
                maximize.hide()
                openInFull.show()
                closeFullScreen.hide()

                content?.show()
            }
            ZkDockedElementState.Maximized -> {
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
            ZkDockedElementState.Minimized -> {
                with(element.style) {
                    width = "max-content"
                    height = "${zkDockStyles.headerHeight}px"
                    margin = "0px"
                }
            }

            ZkDockedElementState.Normal -> {
                // FIXME this is most probably wrong
                with(element.style) {
                    width = "600px"
                    height = "400px"
                    margin = "10px"
                }
            }

            ZkDockedElementState.Maximized -> {
                with(element.style) {
                    width = "${htmlWidth - 20}px"
                    height = "${htmlHeight - 20}px"
                    margin = "10px"
                }
            }
        }
    }

    open fun onMinimize() {
        update(ZkDockedElementState.Minimized)
    }

    open fun onMaximize() {
        update(ZkDockedElementState.Normal)
    }

    open fun onOpenInFullIcon() {
        update(ZkDockedElementState.Maximized)
    }

    open fun onCloseFullScreen() {
        update(ZkDockedElementState.Normal)
    }

    open fun onClose() {
        application.dock -= this
    }

}