/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val kodomatStyles by cssStyleSheet(KodomatStyles())

class KodomatStyles : ZkCssStyleSheet() {

    val smallInput by cssClass {
        width = "5em"
    }

    val mediumInput by cssClass {
        width = "10em"
    }

}