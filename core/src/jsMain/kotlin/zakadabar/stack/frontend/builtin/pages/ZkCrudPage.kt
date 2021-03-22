/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.frontend.builtin.ZkElementMode

interface ZkCrudPage<T> {
    var dto: T
    var mode: ZkElementMode
    var openUpdate: ((dto: T) -> Unit)?
}