/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc.account

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkAccountStyles by cssStyleSheet(ZkAccountStyles())

class ZkAccountStyles : ZkCssStyleSheet() {

    val avatar by cssClass {
        width = 28
        height = 28
        fontSize = 14
        textAlign = "center"
        lineHeight = 28
        borderRadius = "50%"
    }

}