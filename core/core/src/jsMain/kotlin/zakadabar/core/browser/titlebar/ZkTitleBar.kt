/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.titlebar

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.util.PublicApi

@Deprecated("EOL: 2021.8.1  -  use a local or the application title instead")
open class ZkTitleBar() : ZkElement() {

    val _title = ZkElement()

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
