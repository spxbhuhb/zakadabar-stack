/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.crud

import zakadabar.stack.frontend.builtin.ZkElementMode

/**
 * Elements capable of editing the DTO content implement this interface.
 */
interface ZkCrudEditor<T> {
    var dto: T
    var mode: ZkElementMode
    var openUpdate: ((dto: T) -> Unit)?
}