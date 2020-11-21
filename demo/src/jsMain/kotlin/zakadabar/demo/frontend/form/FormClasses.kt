/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.form

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class FormClasses(theme: Theme) : CssStyleSheet<FormClasses>(theme) {

    companion object {
        val formClasses = FormClasses(FrontendContext.theme).attach()
    }

    val selectedEntry by cssClass {
        width = 200
        height = 32
        border = "1px solid lightgray"
    }

    val entryList by cssClass {
        width = 200
        height = 200
        border = "1px solid lightgray"
    }


}