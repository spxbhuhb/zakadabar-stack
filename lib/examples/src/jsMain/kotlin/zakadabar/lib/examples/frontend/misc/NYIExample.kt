/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.misc

import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.misc.NYI
import zakadabar.core.browser.note.noteSecondary

class NYIExample(
    element: HTMLElement
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        + noteSecondary("NYI Example", zke {
            + column {
                + NYI("this will be the header")
                + row {
                    + NYI("first item")
                    + NYI("second item")
                    + NYI("third item")
                }
                + NYI("this will be the footer")
            }
        })

    }

}