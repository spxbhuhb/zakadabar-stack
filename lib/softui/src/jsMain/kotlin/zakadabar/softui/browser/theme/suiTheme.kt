/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.theme.softui.components

import zakadabar.core.resource.ZkTheme
import zakadabar.core.resource.theme

val suiTheme: SuiTheme
    get() = theme as SuiTheme

interface SuiTheme : ZkTheme {
    val headerTagColor : String
    val backgroundImage : String
    val colorOnImage : String
    val sectionBorder : String
}