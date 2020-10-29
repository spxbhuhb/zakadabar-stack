/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.mystyle.frontend

import zakadabar.samples.mystyle.frontend.SampleClasses.Companion.sampleClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.minusAssign
import zakadabar.stack.frontend.elements.plusAssign

class StyleSamples : ZkElement() {

    override fun init(): ZkElement {

        val classes = sampleClasses

        // You can use the "className" and "classList" properties of
        // Element, HTMLElement, SimpleElement, ComplexElement.

        className = "my-very-own-and-very-non-existing-class"
        classList += "yet-another-imaginary-class-now-we-have-two"
        classList -= "this-i-really-want-to-remove"

        this cssClass classes.aRealOne build {

            // The "cssClass" function works on Elements, HTMLElements, SimpleElements
            // and ComplexElements, it is quite handy.

            + div() cssClass classes.box1 build {
                + "this is displayed in a random color: "
                // I use "!" here because otherwise the two strings are added together :)
                ! "blah blah blah" cssClass classes.sillyColor
            }

            // You can use "with" to avoid typing "classes." again and again.
            // If you use "with" read up on it, it's tricky, it can hide members
            // of your class if there are name collisions.

            with(sampleClasses) {

                // You can use the method call if you prefer, it is the same
                // with Kotlin infix functions. I like to avoid all the dots
                // and parenthesis tho as without them the code is much more
                // readable.

                + div().cssClass(box2) build {

                    + "this is big and bold" cssClass classes.bigBoldWolf

                }

            }

        }

        return this
    }

}