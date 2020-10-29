/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

import kotlinx.browser.document
import zakadabar.samples.mystyle.frontend.StyleSamples
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.elements.ZkElement

/**
 * The main method of the web browser application. Check the "hello world" sample for a
 * pretty, commented and more sophisticated version. This one just initializes one
 * component without anything else.
 */
fun main() {

    // add KClass names as data attributes to DOM elements, useful for debugging, not meant for production
    // See: https://github.com/spxbhuhb/zakadabar-stack/blob/master/doc/misc/Productivity.md#simpleelement-addkclass

    ZkElement.addKClass = true

    // This works, but I think it is not right because it depends on the initialization
    // order of classes. We'll have to clean this up and make sure that it is OK.

    FrontendContext.theme.lightColor = "lightblue"

    document.body?.appendChild(StyleSamples().init().element)

}