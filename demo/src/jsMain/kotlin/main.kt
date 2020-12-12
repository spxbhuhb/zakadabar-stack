/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused") // main is called by webpack

import zakadabar.demo.frontend.Routing
import zakadabar.stack.data.builtin.SessionDto
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.Executor
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch

/**
 * The main method of the web browser application. Stared by the bundled Javascript file
 * (demo.js in this case).
 */
fun main() {

    // Launch starts something in the background. Think setTimeout(0, ...) in JavaScript.
    // This launch method starts the task in GlobalScope, and in case an exception is thrown
    // it tries to format it to be a bit more readable.

    launch {

        // Add KClass names as data attributes to DOM elements, useful for debugging, not meant for production.

        ZkElement.addKClass = true

        // Get the session from the server. Note that this is a suspend function, so the code below
        // will be executed only when it finishes.

        val session = SessionDto.read(0L)

        with(Application) {

            // Application.executor is the user who runs the application. There is always a user
            // even without login. The not logged in users has the role "anonymous" (by convention).

            executor = Executor(session.accountId, session.displayName, session.roles)

            // Set the routing. You may change this on-the-fly if you want, for example if the user logs in.

            routing = Routing

            // Initializes the Application and opens the page selected by the URL.

            init()
        }
    }

}