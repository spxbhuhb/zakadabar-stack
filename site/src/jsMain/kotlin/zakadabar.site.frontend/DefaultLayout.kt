/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.site.frontend.components.SiteHeader
import zakadabar.softui.browser.layout.SuiDefaultLayout

object DefaultLayout : SuiDefaultLayout() {
    override fun onCreate() {
        super.onCreate()
        header = SiteHeader()
        sideBar = SideBar()
        pageTitleContainer.hide()
    }
}

