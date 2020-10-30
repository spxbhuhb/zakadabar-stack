/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.waystohtml.frontend

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.util.Initials
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.elements.marginBottom

class HtmlSamples : ZkElement() {

    override fun init(): ZkElement {

        // build this element use coreClasses.contentColumn to align the content to middle

        this cssClass coreClasses.contentColumn build {

            // use coreClasses.mainContent to set it fixed width and suited for content

            + div() cssClass coreClasses.mainContent build {

                // add a gap to the top, so the content won't start at the very top of the page

                + gap(height= 20)

                + t("Add text directly, creates a text node and appends it, no need for escape.")

                ! t("Add text directly as a SPAN, sets innerHTML, <b>not escaped</b>.")

                + gap(height = 10)

                // Add an image, there are different signatures for this method to make it more
                // convenient. For example, you can add an image which is stored as an entity
                // by using it's id instead of the URL.

                + image("roads.jpg") {
                    with(htmlElement as HTMLImageElement) {
                        width = 500
                        height = 432
                        alt = t("roads")
                        longDesc = "Some long description of the image"
                    }
                }

                + gap(height = 10)

                + column { // creates a flex column with its own builder

                    + Way1() marginBottom "1em" // appends a SimpleElement with 1em margin at the bottom

                    + Way2() marginBottom 20 // appends another SimpleElement with a 20 pixels margin bottom

                    + row { // creates a flex row with its own builder, appends it to the column

                        // set styles directly
                        htmlElement.style.flexGrow = "1"

                        + Way3() marginRight 20 // appends a ComplexElement to the row with 20 pixels margin
                        + Way4(t("this is inside")).marginRight(20) // appends another ComplexElement to the row

                        // adds a div with a specific css class, the effect is same as from the
                        // direct setting of the style above

                        + div() cssClass coreClasses.grow build {
                            // use a utility component from the Stack, note the missing translation here, this is a name
                            + Initials("Buzz Lightyear")
                        }
                    }
                } marginBottom 20 // you can use the usual helpers on normal HTML elements also

            }
        }

        val manual = document.createElement("div") as HTMLElement
        manual.innerHTML = """
|           Just plain old the DOM with HTMLElements.<br>
|           Outside of the content, that's why it is aligned differently.<br>
|           I don't really like to build DOM this way because indenting<br>
|           this long text is cumbersome. But it works.<br><br>
|       """.trimMargin() // IMPORTANT this trimMargin has nothing to do with CSS

        element.appendChild(manual)

        // These demonstrate a very dynamic approach: you can pass whatever the
        // element system can handle and Way5 will sort it out. The difference
        // between this and using the builder is that with the builder the
        // compiler does the sorting during compilation time. This way your code
        // does the sorting when init runs.

        this += Way5("text stuff").marginBottom(20)
        this += Way5(Initials("non-text stuff"))

        return super.init()
    }

    class Way1 : ZkElement() {
        override fun init(): ZkElement {
            innerHTML = "Setting the innerHTML directly, from the <b>Kotlin class initialization</b>"
            return this
        }
    }

    class Way2 : ZkElement() {
        override fun init(): ZkElement {
            innerText = "Setting innerText directly, from the <b>Kotlin class initialization</b>"
            return this
        }
    }

    class Way3 : ZkElement() {

        private val icon = Icons.account_box.simple16 // create a simple icon, nothing happens when clicked on

        override fun init(): ZkElement {
            this += icon // You can add elements to "this", in case Way3
            return this
        }
    }

    /**
     * In this case we pass a string and use it in the builder of the element.
     */
    class Way4(private val whatsInside: String) : ZkElement() {

        override fun init(): ZkElement {
            this build {
                ! whatsInside
            }
            return this
        }
    }

    /**
     * Here, we pass something and then sort it out. This is actually pretty
     * ugly, I wouldn't use it this way without good reason.
     */
    class Way5(private val whatsInside: Any) : ZkElement() {

        override fun init(): ZkElement {
            this build {
                when (whatsInside) {
                    is String -> + whatsInside
                    is HTMLElement -> + whatsInside
                    is ZkElement -> + whatsInside
                    else -> throw IllegalArgumentException()
                }
            }
            return this
        }
    }
}