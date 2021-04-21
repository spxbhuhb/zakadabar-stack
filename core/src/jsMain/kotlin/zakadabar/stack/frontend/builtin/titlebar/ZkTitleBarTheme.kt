/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import kotlinx.serialization.Serializable

@Serializable
data class ZkTitleBarTheme(
    var height: String,
    var appHandleBackground: String,
    var appHandleForeground: String,
    var appHandleBorder: String,
    var titleBarBackground: String,
    var titleBarForeground: String,
    var titleBarBorder: String
)