/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend

import zakadabar.core.browser.application.ZkAppLayout
import zakadabar.core.browser.layout.zkLayoutStyles

object PrintLayout : ZkAppLayout("print") {

    init {
        - zkLayoutStyles.layout
    }
}