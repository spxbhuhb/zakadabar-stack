/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages

import zakadabar.stack.data.record.RecordDto

/**
 * Provides common functions used in most CRUD implementations.
 */
@Deprecated("use zakadabar.stack.frontend.builtin.crud.ZkCrudTarget instead")
open class ZkCrudTarget<T : RecordDto<T>> : zakadabar.stack.frontend.builtin.crud.ZkCrudTarget<T>()
