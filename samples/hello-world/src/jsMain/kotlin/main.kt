/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

import kotlinx.browser.document
import zakadabar.samples.helloworld.frontend.HelloWorldCenter
import zakadabar.samples.helloworld.frontend.Module
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.application.navigation.Navigation
import zakadabar.stack.frontend.builtin.desktop.Desktop
import zakadabar.stack.frontend.elements.SimpleElement
import zakadabar.stack.frontend.util.launch

/**
 * The main method of the web browser application. Stared by the bundled Javascript file
 * (hello-world.js) in this case.
 */
fun main() {

    // Launch starts something in the background. Think setTimeout(0, ...) in JavaScript.
    // This launch method starts the task in GlobalScope, and in case an exception is thrown
    // it tries to format it to be a bit more readable.

    launch {

        // add KClass names as data attributes to DOM elements, useful for debugging, not meant for production
        // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/misc/Productivity.md#simpleelement-addkclass

        SimpleElement.addKClass = true

        // Initialize the frontend. This method needs a running backend because it
        // fetches the account of the user who runs the frontend.
        // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/cookbook/common/Accounts.md

        FrontendContext.init()

        // Add modules to the frontend

        FrontendContext += zakadabar.stack.frontend.Module
        FrontendContext += Module

        // Create an instance of Desktop with our own center implementation.

        val desktop = Desktop(center = HelloWorldCenter())

        // Add the desktop to the document bode and initialize it.
        // The order is not particularly important in this case but the convention is to
        // add the element to the document first and then call init.

        document.body?.appendChild(desktop.element)
        desktop.init()

        // Kick off the navigation. This will check the current URL in the window and
        // open the view / page that belongs to that URL.
        // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/cookbook/common/URLs.md
        // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/cookbook/frontent/Navigation.md

        Navigation.init()
    }

}