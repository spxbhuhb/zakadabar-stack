/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import kotlinx.serialization.Serializable

@Serializable
data class ZkTitleBarTheme(
    var appTitleBarHeight: String,
    var localTitleBarHeight: String,
    var appHandleBackground: String,
    var appHandleText: String,
    var appHandleBorder: String,
    var titleBarBackground: String,
    var titleBarText: String,
    var titleBarBorder: String
)