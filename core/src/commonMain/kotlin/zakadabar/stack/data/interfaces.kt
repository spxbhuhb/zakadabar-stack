/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data

import zakadabar.stack.data.entity.EntityId

interface SelectOptionProvider<T> {
    suspend fun asSelectOptions() : List<Pair<EntityId<T>, String>>
}