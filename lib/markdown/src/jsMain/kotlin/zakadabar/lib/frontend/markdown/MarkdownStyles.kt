/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.frontend.markdown

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

class MarkdownStyles : ZkCssStyleSheet<MarkdownStyles>(ZkApplication.theme) {

    val codeFence by cssClass {
        borderRadius = 4
        paddingLeft = 4
        paddingRight = 4
        backgroundColor = ZkColors.Gray.c300
        fontFamily = "monospace"
        fontWeight = "300"
    }

    val codeSpan by cssClass {
        borderRadius = 4
        paddingLeft = 4
        paddingRight = 4
        backgroundColor = ZkColors.Gray.c300
        fontFamily = "monospace"
        fontWeight = "300"
    }

    val inlineLink by cssClass {
        color = ZkColors.LightBlue.c700
    }

    val paragraph by cssClass {
        marginBottom = 10
    }

    init {
        attach()
    }

}