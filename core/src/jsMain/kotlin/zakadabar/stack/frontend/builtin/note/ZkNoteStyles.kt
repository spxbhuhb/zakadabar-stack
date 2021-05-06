/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.note

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkNoteStyles : ZkCssStyleSheet<ZkTheme>() {

    val info by cssClass {
        borderLeft = "2px solid ${theme.color.info}"
        backgroundColor = theme.note.background
    }

    val title by cssClass {
        borderBottom = "1px solid ${theme.color.info}"
        fontWeight = 500
        paddingLeft = theme.layout.spacingStep / 2
        paddingTop = theme.layout.spacingStep / 4
        paddingBottom = theme.layout.spacingStep / 4
    }

    val content by cssClass {
        padding = theme.layout.spacingStep / 2
    }

    init {
        attach()
    }
}