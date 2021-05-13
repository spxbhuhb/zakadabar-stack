/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.theme

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet
import zakadabar.stack.util.PublicApi

val zkHtmlStyles by cssStyleSheet(ZkHtmlStyles())

open class ZkHtmlStyles : ZkCssStyleSheet() {

    @PublicApi
    open val bodyStyle by cssRule("body") {
        fontFamily = theme.fontFamily
        fontSize = theme.fontSize
        fontWeight = theme.fontWeight
        backgroundColor = theme.backgroundColor
        color = theme.textColor
    }

    @PublicApi
    open val aStyle by cssRule("a") {
        color = theme.textColor
        textDecoration = "none"
    }

}