/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

import zakadabar.rui.runtime.RuiFragment

fun RuiTestEvents.report(fragment: RuiFragment, vararg arguments : Any) {
    (fragment.ruiAdapter as RuiTestAdapter).events += RuiTestEvent(this, fragment, *arguments).also {
        println(it.dump())
    }
}