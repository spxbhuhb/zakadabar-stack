/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import kotlinx.serialization.Serializable
import zakadabar.core.util.PublicApi

@PublicApi
@Serializable
class BooleanValue(var value: Boolean) : BaseBo

@PublicApi
@Serializable
class DoubleValue(var value: Double) : BaseBo

@PublicApi
@Serializable
class FloatValue(var value: Float) : BaseBo

@PublicApi
@Serializable
class IntValue(var value: Int) : BaseBo

@PublicApi
@Serializable
class LongValue(var value: Long) : BaseBo

@PublicApi
@Serializable
class StringValue(var value: String) : BaseBo

@PublicApi
fun String.toStringValue() = StringValue(this)