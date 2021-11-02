/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import org.w3c.dom.HTMLTableRowElement

class ZkTableRow<T>(
    var data: T,
    var element: HTMLTableRowElement? = null,
    var searchData: Array<String>? = null
)
