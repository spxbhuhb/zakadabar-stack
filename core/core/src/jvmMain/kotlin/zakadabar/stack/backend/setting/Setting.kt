/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.setting

import kotlinx.serialization.KSerializer
import zakadabar.stack.backend.server
import zakadabar.stack.data.BaseBo
import kotlin.reflect.KProperty

class Setting<V : BaseBo>(
    var value: V,
    val namespace: String,
    private val serializer: KSerializer<V>
) {

    companion object {
        val noProvider = """
            Setting provider cannot be found. This happens when you try to use
            settings from: onModuleLoad, constructor, init block. Move setting
            access to onModuleStart.
        """.trimIndent().replace("\r", " ").replace("\n", " ")
    }

    private var initialized: Boolean = false

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        synchronized(this) {
            if (! initialized) {
                value = server.firstOrNull<SettingProvider>()?.get(value, namespace, serializer) ?: throw IllegalStateException(noProvider)
                initialized = true
            }
        }
        return value
    }

}