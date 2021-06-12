/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

/**
 * Overview of contents.
 *
 * @param  locales   All locales known by the application.
 * @param  entries   List of master content entities.
 */
@Serializable
class ContentOverview(
    val locales : List<LocaleBo>,
    val entries : List<ContentOverviewEntry>
): BaseBo

/**
 * Represents a master content entity.
 *
 * @param   id             Id of the master content entity.
 * @param   title          Title of the master entity.
 * @param   stereotype     Stereotype of the master entity.
 * @param   status         Status of the master entity.
 * @param   localizations  A list of localized versions or null if there is no
 *                         localized version of the given locale. Entries of this
 *                         list are ordered the same as [Overview.locales].
 */
@Serializable
class ContentOverviewEntry(
    val id : EntityId<ContentBo>,
    val title : String,
    val stereotype: String,
    val status: String,
    val localizations : List<EntityId<ContentBo>?>
) : BaseBo