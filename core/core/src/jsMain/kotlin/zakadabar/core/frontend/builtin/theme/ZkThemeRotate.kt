/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.theme

import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.titlebar.zkTitleBarStyles
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIconSource
import zakadabar.core.frontend.resources.ZkTheme
import zakadabar.core.frontend.resources.css.ZkCssStyleRule
import zakadabar.core.frontend.resources.theme

/**
 * Rotates themes.
 */
open class ZkThemeRotate(
    vararg val options: Pair<ZkIconSource, ZkTheme>,
    buttonStyle: ZkCssStyleRule = zkTitleBarStyles.iconButton
) : ZkElement() {

    open val buttons = options.mapIndexed { index, option ->
        ZkButton(option.first, flavour = ZkFlavour.Custom) {
            theme = option.second
            switchTo(index + 1)
        } css buttonStyle
    }

    private fun switchTo(index: Int) {
        this -= firstOrNull<ZkButton>()
        + buttons[index % buttons.size]
    }

    override fun onResume() {
        this -= firstOrNull<ZkButton>()

        for (i in options.indices) {
            if (options[i].second.name == theme.name) {
                + buttons[(i + 1) % buttons.size]
                return
            }
        }
        + buttons.firstOrNull()
    }

}