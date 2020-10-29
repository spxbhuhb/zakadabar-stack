/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.witchesbrew.frontend

import kotlinx.coroutines.delay
import zakadabar.stack.frontend.builtin.simple.SimpleText
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch

class DeepInTheForest : ZkElement() {

    override fun init(): ZkElement {

        val sometimesThis = SimpleText(" is this the shortest route")
        val sometimesThat = SimpleText(" are we lost")

        val wrapper = ZkElement()

        build {
            + div {
                + row {
                    ! "we are deep in the forest ...&nbsp;"
                    + wrapper
                    + " ?"
                }
            }
        }

        var round = 0

        launch {
            while (true) {
                delay(2000)
                wrapper -= SimpleText::class
                wrapper += if (round % 2 == 0) sometimesThat else sometimesThis
                round ++
            }
        }

        return this
    }
}