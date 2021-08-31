/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import zakadabar.core.data.BaseBo
import zakadabar.core.data.UNINITIALIZED
import zakadabar.core.module.modules
import kotlin.reflect.KProperty

open class Setting<V : BaseBo>(
    open val default: V,
    open val namespace: String,
    open val serializer: KSerializer<V>
) {
    open var value: Any? = UNINITIALIZED

    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        if (value === UNINITIALIZED) {
            val module = modules.firstOrNull<SettingProvider>()
            runBlocking {
                mutex.withLock {
                    // This second check is protected by the mutex. If another thread tries to change
                    // initialize the value at the same time, it will wait until this one finishes.
                    // Then it goes into the protected block and realize that the value is already set.
                    if (value === UNINITIALIZED) {
                        value = module
                            ?.get(default, namespace, serializer)
                            ?: throw IllegalStateException(noProvider)
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        return value as V
    }

    companion object {

        val mutex = Mutex()

        val noProvider = """
            Setting provider cannot be found. This happens when you try to use
            settings from: onModuleLoad, constructor, init block. Move setting
            access to onModuleStart.
        """.trimIndent().replace("\r", " ").replace("\n", " ")
    }

}