/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val exampleStyles by cssStyleSheet(ExampleStyles())

class ExampleStyles : ZkCssStyleSheet() {

    val unMarkdownBlock by cssClass {
        border = theme.blockBorder
        backgroundColor = theme.blockBackgroundColor

        on(" a") {
            color = "${theme.textColor} !important"
        }
    }

}