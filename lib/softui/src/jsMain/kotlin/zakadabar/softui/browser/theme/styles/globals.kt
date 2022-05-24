/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.titlebar.zkTitleBarStyles
import zakadabar.core.resource.css.cssStyleSheet

var suiLayoutStyles by cssStyleSheet(SuiLayoutStyles())

val suiTitleBarStyles
   get() = zkTitleBarStyles as SuiTitleBarStyles