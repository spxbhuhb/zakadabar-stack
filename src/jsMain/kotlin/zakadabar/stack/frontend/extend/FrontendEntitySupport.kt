/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.extend.DtoWithEntityContract
import zakadabar.stack.extend.RestCommContract
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.IconSource
import zakadabar.stack.frontend.comm.rest.RestComm
import zakadabar.stack.util.UUID

/**
 * Stores frontend objects that support an entity type. Instances of
 * [FrontendEntitySupport] are added to the
 * [FrontendContext][zakadabar.stack.frontend.FrontendContext] by module loaders.
 *
 * @param     module       The module that provides this entity type.
 * @property  companion    The companion object of the entity DTO.
 * @property  comm         The [RestComm] for the DTO (defaults to standard RestComm for the companion).
 * @property  type         Type of the entity (defaults to [companion] type).
 * @property  displayName  Type name to display to the user.
 * @property  iconSource   Type icon to display to the user.
 * @property  newView      The view to use for new entity creation.
 * @property  navView      The view to use in Navigation.
 * @property  preView      The view to use when displaying a preview or summary of th entity.
 * @property  readView     The view to use when displaying the entity in read-only mode.
 * @property  editView     The view to use when displaying the entity in edit mode.
 * @property  removeView   The view to use when the user wants to remove the entity.
 */
class FrontendEntitySupport<T : DtoWithEntityContract<T>>(
    module: UUID,
    val companion: DtoWithEntityCompanion<T>,
    val comm: RestCommContract<T>? = RestComm(companion.type, companion.serializer()),
    val type: String = companion.type,
    val displayName: String = t(companion.type, namespace = module),
    val iconSource: IconSource? = null,
    val newView: ScopedViewContract? = null,
    val navView: ScopedViewContract? = null,
    val preView: ScopedViewContract? = null,
    val readView: ScopedViewContract? = null,
    val editView: ScopedViewContract? = null,
    val removeView: ScopedViewContract? = null
) {

    init {
        if (comm != null) companion.comm = comm
    }

}