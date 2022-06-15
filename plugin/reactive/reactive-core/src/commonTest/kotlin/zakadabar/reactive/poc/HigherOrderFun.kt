/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.reactive.poc

import zakadabar.reactive.core.ReactiveComponent

fun HigherOrderFun(builder: () -> Unit) {
    builder()
}

class ReactiveHigherOrderFun(
    var builder: () -> ReactiveComponent
): ReactiveComponent() {

    var block0 = builder()

    override fun patch(mask: Array<Int>) {
        block0.patch(arrayOf(0))
    }

}