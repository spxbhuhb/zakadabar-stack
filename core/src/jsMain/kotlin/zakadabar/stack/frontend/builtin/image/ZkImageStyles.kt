/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.image

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkImageStyles : CssStyleSheet<ZkImageStyles>(Application.theme) {

    val outerView by cssClass {
        boxSizing = "border-box"
        position = "absolute"
        top = "0"
        left = "0"
        height = "100vh"
        width = "100vw"
        display = "flex"
        background = "rgba(0, 0, 0, 0.8)"
        zIndex = 1000
        justifyContent = "center"
        alignItems = "center"
        outline = "none" // this is here because we have a tabindex on ZkFullScreenImageView
    }

    val image by cssClass {
        maxWidth = "100%"
        maxHeight = "100%"
    }

    val actions by cssClass {
        display = "flex"
        flexDirection = "row"
        justifyContent = "space-around"
    }

    val closeButton by cssClass {
        position = "absolute"
        top = 20
        right = 20
        zIndex = 1001
    }

    val deleteButton by cssClass {
        position = "absolute"
        top = 20
        left = 20
        zIndex = 1001
    }

    init {
        attach()
    }

}