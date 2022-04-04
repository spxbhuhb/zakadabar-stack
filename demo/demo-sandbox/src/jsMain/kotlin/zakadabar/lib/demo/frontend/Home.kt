/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import zakadabar.core.browser.page.ZkPage
import zakadabar.core.browser.page.zkPageStyles
import zakadabar.core.resource.css.Position

object Home : ZkPage(css = zkPageStyles.fixed) {

    override fun onCreate() {
        + Position.relative

        fun greet() = listOf("Hello", "Hallo", "Hola", "Servus").random()

        + div {
            renderComposable(buildPoint) {
                val greeting = remember { mutableStateOf(greet()) }
                Button(attrs = { onClick { greeting.value = greet() } }) {
                    Text(greeting.value)
                }
            }
        }
    }
}