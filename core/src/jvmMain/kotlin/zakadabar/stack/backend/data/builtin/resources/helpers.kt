/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:JvmName("SettingKt")

package zakadabar.stack.backend.data.builtin.resources

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import zakadabar.stack.backend.util.default
import zakadabar.stack.data.DtoBase
import kotlin.reflect.full.createType

@Suppress("UNCHECKED_CAST") // serializer should create KSerializer<T> for sure
inline fun <reified T : DtoBase> setting(namespace: String) =
    Setting(default(), namespace, serializer(T::class.createType()) as KSerializer<T>)
