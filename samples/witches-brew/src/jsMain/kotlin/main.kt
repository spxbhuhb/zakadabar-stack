/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

import kotlinx.browser.document
import zakadabar.samples.witchesbrew.frontend.Cauldron
import zakadabar.stack.frontend.elements.SimpleElement

/**
 * The main method of the web browser application. Check the "hello world" sample for a
 * pretty, commented and more sophisticated version. This one just initializes one
 * component without anything else.
 */
fun main() {

    SimpleElement.addKClass = true

    document.body?.appendChild(Cauldron().init().element)

}