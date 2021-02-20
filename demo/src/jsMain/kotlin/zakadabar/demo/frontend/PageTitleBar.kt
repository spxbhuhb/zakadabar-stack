/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.stack.frontend.elements.ZkElement

object PageTitleBar : ZkElement() {

    init {
        style {
            height = "44px" // linked to ZkMenuStyles.title.height
            backgroundColor = "rgb(245,245,245)"
            borderBottom = "0.5px solid #ccc"
            display = "flex"
            flexDirection = "row"
            alignItems = "center"
            paddingLeft = "20px"
            fontSize = "16px"
        }

        + "Hello"
    }

}



