/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.helloworld.frontend

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.util.Dictionary

/**
 * A helper module we use
 */
fun thw(text: String) = t(text, Module.uuid)

val translations: Dictionary = mutableMapOf(
    "hu" to mutableMapOf(
        "click.on.something" to "kattints a bal oldalon előnézethez",
        "dbclick.on.something" to "kattints duplán megnyitáshoz",
        "welcome" to "Üdvözlünk Zakadabarban!",
    ),
    "en" to mutableMapOf(
        "click.on.something" to "click on the left for preview",
        "dbclick.on.something" to "double click to open",
        "welcome" to "Welcome to Zakadabar!",
    )
)