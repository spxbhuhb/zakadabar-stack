/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */

package zakadabar.lib.examples.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

@Serializable
data class ExampleResult(
    val EntityId: EntityId<BuiltinBo>,
    var booleanValue: Boolean,
    var enumSelectValue: ExampleEnum,
    var intValue: Int,
    var stringValue: String
) : BaseBo