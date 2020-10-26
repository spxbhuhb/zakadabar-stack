/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.mystyle.frontend

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme
import zakadabar.stack.util.fourRandomInt
import kotlin.math.max
import kotlin.math.min

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
class SampleClasses(theme: Theme) : CssStyleSheet<SampleClasses>(theme) {

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
        val sampleClasses = SampleClasses(FrontendContext.theme).attach()
    }

    val aRealOne by cssClass {
        marginLeft = 100
        marginTop = 100
        width = "50%" // Some values accept Any. Int will be converted into "px", others will be uses as String
        height = 600 // This will be "600px"
        maxHeight = "600px !important" // You can add important if you wish
        border = "1px solid red"
    }

    val box1 by cssClass {
        margin = 4
        padding = 4
        border = "1px solid green"
    }

    val box2 by cssClass {
        display = "flex"
        flexDirection = "column"
        justifyContent = "flex-start" // If you hover your mouse above justifyContent you'll get help.
        alignItems = "center" // In case you always forget which is which. :D
        backgroundColor = theme.lightColor // Use a color from the theme.
        margin = 4
        padding = 4
    }

    val bigBoldWolf by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 700
    }

    // let's play a bit around

    val sillyColor by cssClass {

        val v = fourRandomInt().map { min(it, 255) }

        color = "rgba(${v[0]},${v[1]},${v[2]},1)"

    }

}