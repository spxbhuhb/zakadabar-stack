/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.helloworld.frontend

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
class HelloWorldClasses(theme: Theme) : CssStyleSheet<HelloWorldClasses>(theme) {

    companion object {
        /**
         * We will use this field to access the CSS style sheet. Defining it as
         * `val` means that it cannot be overridden, code in this module will
         * use exactly this style sheet. You can define it as `var` and then
         * it may be changed.
         */
        val helloWorldClasses = HelloWorldClasses(FrontendContext.theme).attach()
    }

    val welcome by cssClass {
        display = "flex"
        flexDirection = "column"
        justifyContent = "flex-start"
        alignItems = "center"
        height = "100%"
        color = theme.darkestGray
    }

    val welcomeImage by cssClass {
        width = "80%"
        marginBottom = "10%"
        marginTop = "10%"
        borderRadius = 2
    }

    val welcomeTitle by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 20
        fontWeight = 400
        paddingBottom = 6
    }

    val welcomeInstructions by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 400
    }

}