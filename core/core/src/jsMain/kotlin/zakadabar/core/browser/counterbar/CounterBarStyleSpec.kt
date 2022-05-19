/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.counterbar

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface CounterBarStyleSpec : CssStyleSpec {

    var borderTop: String
    var backgroundColor: String

    val tableCounterBar: ZkCssStyleRule

}