/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend

import zakadabar.core.frontend.resources.css.ZkCssStyleSheet
import zakadabar.core.frontend.resources.css.cssStyleSheet

val exampleStyles by cssStyleSheet(ExampleStyles())

class ExampleStyles : ZkCssStyleSheet() {

    val unMarkdown by cssClass {
        on(" a") {
            color = "${theme.textColor} !important"
        }
    }

}