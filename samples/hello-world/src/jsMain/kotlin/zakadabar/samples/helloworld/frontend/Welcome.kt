/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.helloworld.frontend

import zakadabar.samples.helloworld.frontend.HelloWorldClasses.Companion.helloWorldClasses
import zakadabar.stack.frontend.elements.ZkElement

/**
 * Displays the welcome page with the image and the click instructions.
 */
class Welcome : ZkElement() {

    /**
     * This initialization function is called when the welcome element is
     * added to another element in some way. There are different ways to
     * build element structures, most of them calls [init] at some point.
     */
    override fun init(): ZkElement {

        // We use the classes from the source file [HelloWorldClasses].
        // This is just a shorthand for the CSS style.

        val css = helloWorldClasses

        // This is where we build the actual page. It is quite simple actually,
        // don't get scared about the syntax. Check out the sample "5 Ways to HTML"
        // for explanations how this works.

        this cssClass css.welcome build {
            + image("jungle-sea.jpeg", css.welcomeImage)
            + div(css.welcomeTitle) { + thw("welcome") }
            + div(css.welcomeInstructions) { ! """<span style="font-size: 150%">↫</span> ${thw("click.on.something")}""" }
            + div(css.welcomeInstructions) { ! """<span style="font-size: 150%">↫</span> ${thw("dbclick.on.something")}""" }
        }

        // All init methods have to return with the element itself.

        return this
    }

}
