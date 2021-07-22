/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import zakadabar.stack.util.PublicApi

@PublicApi
inline fun <reified T : EntityBo<T>> String.toEntityId() = EntityId<T>(this)