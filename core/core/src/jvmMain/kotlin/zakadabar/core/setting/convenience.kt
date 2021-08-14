/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:JvmName("SettingKt")

package zakadabar.core.setting

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import zakadabar.core.data.BaseBo
import zakadabar.core.util.default
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

fun KClass<*>.shortPackageName() : String {
    var qn = qualifiedName ?: return "unknown"
    qn = qn.substringAfter("zakadabar.").substringBeforeLast('.')
    return qn.substringBeforeLast(".data")
}

@Suppress("UNCHECKED_CAST") // serializer should create KSerializer<T> for sure
inline fun <reified T : BaseBo> setting(namespace: String = T::class.shortPackageName()) =
    Setting(default(), namespace, serializer(T::class.createType()) as KSerializer<T>)
