/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.misc.account

import zakadabar.core.frontend.resources.css.*

val zkAccountStyles by cssStyleSheet(ZkAccountStyles())

class ZkAccountStyles : ZkCssStyleSheet() {

    val avatar by cssClass {
        width = 28.px
        height = 28.px
        fontSize = 14.px
        lineHeight = 28.px
        borderRadius = 50.percent

        + TextAlign.center
    }

}