/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.components

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.site.frontend.resources.SiteStyles
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

class SiteLogo : ZkElement() {

    override fun onCreate() {
        classList += SiteStyles.logo
        io {
            // "fill" from CSS works only for inline SVG, it doesn't work for <img>
            element.innerHTML = window.fetch("/zakadabar.svg").await().text().await()
        }
    }
}