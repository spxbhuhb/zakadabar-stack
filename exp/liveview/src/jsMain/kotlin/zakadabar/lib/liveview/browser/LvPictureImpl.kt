/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.liveview.browser

import org.w3c.dom.HTMLImageElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.css.Position
import zakadabar.core.resource.css.px
import zakadabar.lib.liveview.model.LvPicture

class LvPictureImpl : ZkElement(), LvImpl<LvPicture> {

    override lateinit var data : LvPicture

    override fun onCreate() {
        with (element.style) {
            + Position.absolute
            top = data.y.px
            left = data.x.px
        }

        + image(data.url) {
            (buildPoint as HTMLImageElement).apply {
                width = data.width.toInt()
                height = data.height.toInt()
            }
        }
    }

}