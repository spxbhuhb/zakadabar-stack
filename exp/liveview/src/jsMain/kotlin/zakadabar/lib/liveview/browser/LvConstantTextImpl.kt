/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.liveview.browser

import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.css.Position
import zakadabar.core.resource.css.px
import zakadabar.lib.liveview.model.LvConstantText

class LvConstantTextImpl : ZkElement(), LvImpl<LvConstantText> {

    override lateinit var data : LvConstantText

    override fun onCreate() {
        with (element.style) {
            + Position.absolute
            top = data.y.px
            left = data.x.px
        }
        + data.text
    }

}