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

        on(" .$title") {
            borderBottom = "1px solid ${theme.color.info}"
        }
    }

    val warning by cssClass {
        borderLeft = "2px solid ${theme.color.warning}"
        backgroundColor = theme.note.background

        on(" .$title") {
            borderBottom = "1px solid ${theme.color.warning}"
        }
    }

    val title by cssClass {
        fontWeight = 500
        paddingLeft = theme.spacingStep / 2
        paddingTop = theme.spacingStep / 4
        paddingBottom = theme.spacingStep / 4
    }

    val content by cssClass {
        padding = theme.spacingStep / 2
    }

    init {
        attach()
    }
}