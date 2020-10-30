/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.theplace.frontend

import zakadabar.samples.theplace.ThePlace
import zakadabar.stack.frontend.elements.ZkPage.Companion.buildNewPage

val Home = buildNewPage(ThePlace.shid, "") {
    + "Home"
}