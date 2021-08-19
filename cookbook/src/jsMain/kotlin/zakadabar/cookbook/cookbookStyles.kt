/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import zakadabar.core.resource.css.ZkCssStyleSheet
import zakadabar.core.resource.css.cssStyleSheet
import zakadabar.core.resource.css.px

val cookbookStyles by cssStyleSheet(CookbookStyles())

open class CookbookStyles : ZkCssStyleSheet() {

    val inlineTable by cssClass {
        height = 400.px
        border = theme.fixBorder
    }

    val inlineForm by cssClass {
        maxWidth = 600.px
    }

}