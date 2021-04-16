/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.application.ZkAppLayout
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleBar
import zakadabar.stack.frontend.builtin.titlebar.ZkPageTitle
import zakadabar.stack.frontend.util.plusAssign

open class ZkDefaultLayout : ZkAppLayout("default") {

    open var appHandle = ZkElement()
        set(value) {
            appHandleContainer -= field
            field = value
            appHandleContainer += field
        }

    open var sideBar = ZkElement()
        set(value) {
            sideBarContainer -= field
            field = value
            sideBarContainer += field
        }

    open var titleBar = ZkAppTitleBar()
        set(value) {
            titleBarContainer -= field
            field = value
            titleBarContainer += field
        }

    private var appHandleContainer = ZkElement() gridRow 1 gridColumn 1
    private var sideBarContainer = ZkElement() gridRow 2 gridColumn 1
    private var titleBarContainer = ZkElement() gridRow 1 gridColumn 2

    override fun onCreate() {

        classList += ZkLayoutStyles.defaultLayout // this is a grid layout

        + appHandleContainer build { + appHandle }

        + sideBarContainer build {
            style {
                maxHeight = "100%"
                overflowY = "auto"
            }
            + sideBar
        }

        + titleBarContainer build { + titleBar }

        + content gridRow 2 gridColumn 2

    }

    override fun onResume() {
        super.onResume()
        ZkApplication.onTitleChange = ::onTitleChange
        titleBar.title = ZkApplication.title
    }

    override fun onPause() {
        titleBar.title = null
        super.onPause()
    }

    private fun onTitleChange(newTitle: ZkPageTitle) {
        this.titleBar.title = newTitle
    }
}