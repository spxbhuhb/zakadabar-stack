/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.util.PublicApi
import zakadabar.core.util.default
import zakadabar.core.util.newInstance
import kotlin.reflect.KClass

object UNINITIALIZED

@PublicApi
inline fun <reified T : EntityBo<T>> String.toEntityId() = EntityId<T>(this)

// TODO check type safety for EntityId.read
@Suppress("UNCHECKED_CAST")
suspend inline fun <reified T> EntityId<T>.read(executor : Executor? = null, config : CommConfig? = null) : T {
    val kClass = T::class as KClass<Any>
    return (kClass.newInstance() as EntityBo<T>).comm().read(this, executor, config) // TODO replace newInstance with comm registry
}

suspend inline fun <reified T : EntityBo<T>> EntityBoCompanion<T>.create(executor : Executor? = null, config : CommConfig? = null, func: T.() -> Unit) : T {
    return default {
        func()
    }.create(executor, config)
}