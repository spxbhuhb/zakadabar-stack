/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import kotlinx.serialization.Serializable
import zakadabar.stack.frontend.resources.ZkColors

@Serializable
data class ZkLayoutTheme(
    val spacingStep: Int = 20,
    val sliderColor: String = ZkColors.LightBlue.a700,
    val sliderHoverColor: String = ZkColors.LightBlue.a400,
    val defaultForeground: String = ZkColors.black,
    val defaultBackground: String = ZkColors.Gray.c100
)