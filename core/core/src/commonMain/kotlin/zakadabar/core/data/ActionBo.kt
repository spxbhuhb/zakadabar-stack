/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

interface ActionBo<RESPONSE : Any?> : BaseBo {
    suspend fun execute(): RESPONSE
}
