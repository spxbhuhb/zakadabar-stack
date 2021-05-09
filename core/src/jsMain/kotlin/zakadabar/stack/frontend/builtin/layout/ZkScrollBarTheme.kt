/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import kotlinx.serialization.Serializable

@Serializable
data class ZkScrollBarTheme(
    val width: Int,
    val height: Int,
    val thumb: String,
    val track: String
)