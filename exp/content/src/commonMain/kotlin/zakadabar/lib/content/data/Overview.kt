/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

@Serializable
class Overview(
    val locales : List<LocaleBo>,
    val entries : List<OverviewEntry>
): BaseBo

@Serializable
class OverviewEntry(
    val id : EntityId<ContentCommonBo>,
    val title : String,
    val category: String,
    val status: String,
    val locales : List<EntityId<ContentCommonBo>?>
) : BaseBo