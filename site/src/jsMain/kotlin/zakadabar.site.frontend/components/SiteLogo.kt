/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.plusAssign
import zakadabar.site.frontend.resources.siteStyles

class SiteLogo : ZkElement() {

    override fun onCreate() {
        classList += siteStyles.logo
        io {
            // "fill" from CSS works only for inline SVG, it doesn't work for <img>
            // element.innerHTML = window.fetch("/zakadabar.svg").await().text().await()
            element.innerHTML = "Zakadabar"
        }
    }
}