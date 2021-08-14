/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.pages

import zakadabar.core.data.entity.EntityBo

/**
 * Provides common functions used in most CRUD implementations.
 */
@Deprecated("EOL: 2021.8.1  -  use zakadabar.stack.frontend.builtin.crud.ZkCrudTarget instead")
open class ZkCrudTarget<T : EntityBo<T>> : zakadabar.stack.frontend.builtin.crud.ZkCrudTarget<T>()
