/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.table

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class TableClasses(theme: Theme) : CssStyleSheet<TableClasses>(theme) {

    companion object {
        val tableClasses = TableClasses(FrontendContext.theme).attach()
    }

    val table by cssClass {
        display = "grid"
    }

    val tableRow by cssClass {
        display = "contents"
    }

}