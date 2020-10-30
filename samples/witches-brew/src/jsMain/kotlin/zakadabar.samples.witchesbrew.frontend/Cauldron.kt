/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.witchesbrew.frontend

import zakadabar.stack.frontend.builtin.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement

class Cauldron : ZkElement() {

    override fun init(): ZkElement {

        // build this element use coreClasses.contentColumn to align the content to middle

        this cssClass coreClasses.contentColumn build {
            + RealSimple()
            + DeepInTheForest()
        }

        return this
    }

}