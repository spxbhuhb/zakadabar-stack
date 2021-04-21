/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi

@PublicApi
open class ZkTitleBar() : ZkElement() {

    private val _title = ZkElement()

    var title: String = ""
        set(value) {
            field = value
            _title.innerHTML = field
        }

    override fun onCreate() {
        classList += ZkTitleBarStyles.appTitleBar
        + _title
    }

    @PublicApi
    constructor(title: String) : this() {
        this.title = title
    }


}
