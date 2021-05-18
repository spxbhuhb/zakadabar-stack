/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import org.w3c.dom.HTMLElement
import zakadabar.site.frontend.resources.GreenBlueTheme
import zakadabar.site.resources.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.theme
import zakadabar.stack.frontend.util.marginBottom

class ThemeShowCase(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + row {
            + ZkButton(strings.greenBlue) { theme = GreenBlueTheme() }
        } marginBottom 20

    }

}