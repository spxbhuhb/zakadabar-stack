/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.stack.data.schema.descriptor

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.schema.BoSchema

/**
 * Describes a business object with data and validation rules included. Intended for
 * dynamic business object handling, building forms automatically for example.
 *
 * [BoSchema] may be automatically converted into a [BoDescriptor]
 * by calling [BoSchema.toBoDescriptor].
 */
@Serializable
class BoDescriptor(

    var packageName: String,
    var className: String,
    var boNamespace: String,
    var properties: List<BoProperty>

) : BaseBo