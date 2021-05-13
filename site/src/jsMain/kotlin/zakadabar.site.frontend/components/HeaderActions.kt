/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.site.frontend.resources.siteStyles
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.titlebar.actions.DarkLightMode
import zakadabar.stack.frontend.util.marginRight

class HeaderActions : ZkElement() {

    override fun onCreate() {
        + row(siteStyles.headerActions) {
            + DarkLightMode({ SiteDarkTheme() }, { SiteLightTheme() })
        } marginRight 20
    }

}