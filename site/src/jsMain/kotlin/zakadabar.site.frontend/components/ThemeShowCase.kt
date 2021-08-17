/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import org.w3c.dom.HTMLElement
import zakadabar.site.frontend.resources.SiteGreenBlueTheme
import zakadabar.site.resources.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.resource.theme
import zakadabar.core.browser.util.marginBottom

class ThemeShowCase(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + row {
            + ZkButton(strings.greenBlue) { theme = SiteGreenBlueTheme() }
        } marginBottom 20

    }

}