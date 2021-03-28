/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.site.frontend.Routing
import zakadabar.site.resources.SiteStrings
import zakadabar.stack.data.builtin.account.SessionDto
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkExecutor
import zakadabar.stack.frontend.builtin.ZkBuiltinTheme
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

/**
 * The main method of the web browser application. Stared by the bundled Javascript file
 * (demo.js in this case).
 */
fun main() {

    // Io starts the block behind it in the background. Think setTimeout(0, ...) in JavaScript.

    io {

        // Add KClass names as data attributes to DOM elements, useful for debugging, not meant for production.

        ZkElement.addKClass = true

        // Get the session from the server. Note that this is a suspend function, so the code below
        // will be executed only when it finishes. By definition session id "0" returns with the
        // current session between the frontend and backend. If there is no such session, a new
        // one is created.

        val session = SessionDto.read(0L)

        with(ZkApplication) {

            // Application.executor is the user who runs the application. There is always a user
            // even without login. In this case (not logged in) the anonymous flag is set to true.

            executor = ZkExecutor(session.account, session.anonymous, session.roles)

            // Set the theme for the application. You can create your own theme class
            // or object and set it here.

            theme = ZkBuiltinTheme()

            // Set the string store we want to use. The build-in stack components use this
            // instance to get strings when needed. This is also used to map property names
            // to labels in tables and forms.

            strings = SiteStrings()

            // Set the routing. You may change this on-the-fly if you want, for example if the user logs in.

            routing = Routing

            // Initializes the Application and opens the page selected by the URL.

            init()
        }
    }

}