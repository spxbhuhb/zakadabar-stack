/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.resources

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object Styles : CssStyleSheet<Styles>(Application.theme) {

    init {
        attach()
    }
}