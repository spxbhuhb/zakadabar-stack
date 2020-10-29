/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.application

import kotlinx.browser.document
import zakadabar.stack.frontend.application.navigation.NavTarget
import zakadabar.stack.frontend.application.navigation.Navigation
import zakadabar.stack.frontend.builtin.layout.FullScreen

/**
 * The application that runs in the browser window. This object contains data
 * and resources that are used all over the application.
 */
object Application {

    /**
     * Application layouts, see [AppLayout] for more information.
     */
    val layouts = mutableListOf<AppLayout>()

    /**
     * The default layout. [zakadabar.stack.frontend.application.navigation.NavTarget]s
     * without different layout specified use this layout. to this
     */
    var defaultLayout = FullScreen

    /**
     * Target to use when the URL is '/'. Application startup should set the
     * value.
     */
    lateinit var home: NavTarget

    /**
     * Initializes the application.
     */
    fun init() {

        layouts.forEach {
            document.body?.appendChild(it.element)
            it.hide()
            it.init()
        }

        Navigation.init()
    }
}