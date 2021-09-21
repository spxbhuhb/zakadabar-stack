/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.liveview.browser

import zakadabar.core.browser.ZkElement
import zakadabar.core.resource.css.Position
import zakadabar.core.resource.css.px
import zakadabar.lib.liveview.model.LiveView
import zakadabar.lib.liveview.model.LvConstantText
import zakadabar.lib.liveview.model.LvPicture

class LiveViewImpl : ZkElement(), LvImpl<LiveView> {

    override lateinit var data : LiveView

    override fun onCreate() {

        with (element.style) {
            + Position.absolute
            top = data.y.px
            left = data.x.px
            width = data.width.px
            height = data.height.px
        }

        data.elements.forEach {
            when (it) {
                is LvConstantText -> + LvConstantTextImpl().apply { data = it }
                is LvPicture -> + LvPictureImpl().apply { data = it }
                else -> Unit
            }
        }
    }
}