/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.theme

import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.percent
import zakadabar.core.util.PublicApi

var zkHtmlStyles by cssStyleSheet(ZkHtmlStyles())

open class ZkHtmlStyles : ZkCssStyleSheet() {

    @PublicApi
    open val bodyStyle by cssRule("html, body") {
        width = 100.percent
        height = 100.percent
        margin = "0 !important"
        padding = "0 !important"

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