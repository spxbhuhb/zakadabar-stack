/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import org.w3c.dom.HTMLTableRowElement

class ZkTableRow<T>(
    val data: T,
    var element: HTMLTableRowElement? = null,
    var searchData: Array<String>? = null
)
