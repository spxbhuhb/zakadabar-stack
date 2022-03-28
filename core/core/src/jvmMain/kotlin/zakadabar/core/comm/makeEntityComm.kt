/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion

actual fun <T : EntityBo<T>> makeEntityComm(companion: EntityBoCompanion<T>, config : CommConfig?): EntityCommInterface<T> {
    return EntityComm(companion.boNamespace, companion.serializer(), config)
}