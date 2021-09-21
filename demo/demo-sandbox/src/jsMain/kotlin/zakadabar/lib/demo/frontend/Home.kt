/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.resource.css.Position
import zakadabar.lib.liveview.browser.LiveViewImpl
import zakadabar.lib.liveview.model.LiveView
import zakadabar.lib.liveview.model.LvConstantText
import zakadabar.lib.liveview.model.LvPicture

object Home : ZkPage(css = zkPageStyles.fixed) {

    val lv = LiveView(
        x = 0.0, y = 0.0, width = 800.0, height = 600.0,
        elements = listOf(
            LvConstantText(x = 10.0, y = 10.0, text = "Hello World"),
            LvPicture(x = 10.0, y = 30.0, width = 40.0, height = 40.0, url = "/favicon.png")
        )
    )

    override fun onCreate() {
        + Position.relative
        + LiveViewImpl().apply { data = lv }
    }
}