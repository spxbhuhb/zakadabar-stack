/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

import zakadabar.stack.extend.DtoWithEntityCompanionContract
import zakadabar.stack.extend.DtoWithEntityContract
import zakadabar.stack.extend.EntityRestCommContract

abstract class DtoWithEntityCompanion<T : DtoWithEntityContract<T>> : DtoWithEntityCompanionContract<T> {
    override lateinit var comm: EntityRestCommContract<T>
}
