/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.site.frontend.resources.GreenBlueTheme
import zakadabar.site.frontend.resources.SiteDarkTheme
import zakadabar.site.frontend.resources.SiteLightTheme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.theme.ZkThemeRotate
import zakadabar.stack.frontend.resources.ZkIcons

class HeaderActions : ZkElement() {

    override fun onCreate() {
        + ZkThemeRotate(
            ZkIcons.darkMode to SiteDarkTheme(),
            ZkIcons.lightMode to SiteLightTheme(),
            ZkIcons.leaf to GreenBlueTheme()
        )
    }

}