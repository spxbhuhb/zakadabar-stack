/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi

@Deprecated("EOL: 2021.8.1  -  use a local or the application title instead")
open class ZkTitleBar() : ZkElement() {

    private val _title = ZkElement()

    var title: String = ""
        set(value) {
            field = value
            _title.innerHTML = field
        }

    override fun onCreate() {
        classList += zkTitleBarStyles.appTitleBar
        + _title
    }

    @PublicApi
    constructor(title: String) : this() {
        this.title = title
    }


}
