/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.witchesbrew.frontend

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

/**
 * This is a CSS style sheet. From other pieces of yhe code you can use it by the
 * [helloWorldClasses] field of the companion object.
 *
 * There is some magic involved here, but you don't have to worry about it. At
 * the end you just use the fields defined here. Check [Welcome] for an example
 * of use.
 */
class CauldronClasses(theme: Theme) : CssStyleSheet<CauldronClasses>(theme) {

    companion object {
        /**
         * We will use this field to access the CSS style sheet. Defining it as
         * `val` means that it cannot be overridden, code in this module will
         * use exactly this style sheet. You can define it as `var` and then
         * it may be changed.
         */
        val cauldronClasses = CauldronClasses(FrontendContext.theme).attach()
    }

    val realSimple by cssClass {
        color = theme.darkestGray
    }

    val text1 by cssClass {
        color = "red"
    }

    val text2 by cssClass {
        color = "blue"
    }

}