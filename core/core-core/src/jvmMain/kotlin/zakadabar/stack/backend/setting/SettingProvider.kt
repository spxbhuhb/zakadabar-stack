/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.setting

import kotlinx.serialization.KSerializer
import zakadabar.stack.data.BaseBo

interface SettingProvider {
    fun <T : BaseBo> get(default: T, namespace: String, serializer: KSerializer<T>) : T
}