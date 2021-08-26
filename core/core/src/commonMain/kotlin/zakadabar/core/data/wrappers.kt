/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data

import kotlinx.serialization.Serializable
import zakadabar.core.util.PublicApi

@PublicApi
@Serializable
class BooleanValue(val value: Boolean) : BaseBo

@PublicApi
@Serializable
class DoubleValue(val value: Double) : BaseBo

@PublicApi
@Serializable
class FloatValue(val value: Float) : BaseBo

@PublicApi
@Serializable
class IntValue(val value: Int) : BaseBo

@PublicApi
@Serializable
class LongValue(val value: Long) : BaseBo

@PublicApi
@Serializable
class StringValue(val value: String) : BaseBo

@PublicApi
fun String.toStringValue() = StringValue(this)