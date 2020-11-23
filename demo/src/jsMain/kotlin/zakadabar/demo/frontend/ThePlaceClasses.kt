/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

/**
 * This is a CSS style sheet. From other pieces of yhe code you can use it by the
 * [sampleClasses] field of the companion object.
 *
 * There is some magic involved here, but you don't have to worry about it. At
 * the end you just use the fields defined here.
 *
 * The important thing is that in the Stack CSS classes are plain old Kotlin classes,
 * you can do whatever you want to do with them.
 *
 * In general we expect to have fields of String type with a value that is known
 * as a CSS class by the browser.
 */
class ThePlaceClasses(theme: Theme) : CssStyleSheet<ThePlaceClasses>(theme) {

    companion object {
        /**
         * We will use this field to access the CSS style sheet. Defining it as
         * `val` means that it cannot be overridden, code in this module will
         * use exactly this style sheet. You can define it as `var` and then
         * it may be changed.
         *
         * IMPORTANT ---- Note the attach() at the end! ----
         *
         * That [CssStyleSheet.attach] call adds the style sheet to the browser. There is also
         * a [CssStyleSheet.detach] function that removes the style sheet.
         *
         * This particular call uses the default theme from the [FrontendContext].
         */
        val thePlaceClasses = ThePlaceClasses(FrontendContext.theme).attach()
    }

    val menu by cssClass {
        height = "100%"
        backgroundColor = "#538d34"
        color = "white"
        minWidth = 200
    }

}