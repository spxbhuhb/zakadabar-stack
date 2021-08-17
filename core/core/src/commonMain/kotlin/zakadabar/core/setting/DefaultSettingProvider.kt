/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.setting

import kotlinx.serialization.KSerializer
import zakadabar.core.data.BaseBo
import zakadabar.core.util.Lock
import zakadabar.core.util.use
import kotlin.collections.set
import kotlin.reflect.KClass

/**
 * Setting provider that simply returns with the defaults.
 */
open class DefaultSettingProvider : SettingProvider {

    open val lock = Lock()

    protected val instances = mutableMapOf<Pair<String, KClass<out BaseBo>>, BaseBo>()

    @Suppress("UNCHECKED_CAST") // key contains class
    override fun <T : BaseBo> get(default: T, namespace: String, serializer: KSerializer<T>) : T {
        val key = namespace to default::class

        lock.use {
            instances[key]?.let { return it as T }
            instances[key] = default
            return default
        }
    }

}