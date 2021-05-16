/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(OptInstantAsStringSerializer::class)

package zakadabar.stack.data.schema.descriptor

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.schema.DtoSchema
import zakadabar.stack.data.util.OptInstantAsStringSerializer

/**
 * Describes a DTO with data and validation rules included. Intended for
 * "dynamic" DTO handling, building forms automatically for example.
 *
 * [DtoSchema] may be automatically converted into a [DescriptorDto]
 * by calling [DtoSchema.toDescriptorDto].
 */
@Serializable
data class DescriptorDto(

    var packageName : String,
    var kClassName : String,
    var dtoNamespace : String,
    var properties: List<PropertyDto>,

) : DtoBase