/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.help

import zakadabar.core.resource.css.*

val zkHelpStyles by cssStyleSheet(ZkHelpStyles())

open class ZkHelpStyles : ZkCssStyleSheet() {

    val withHelpContainer by cssClass {
        + Display.inlineFlex
        + FlexDirection.row
    }

    val helpButton by cssClass {
        marginLeft = (theme.spacingStep / 2).px
    }

}