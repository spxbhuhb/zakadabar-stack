/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.data.entity.DtoWithEntityCompanion
import zakadabar.stack.extend.DtoWithEntityContract
import zakadabar.stack.extend.EntityRestCommContract
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.icon.IconSource
import zakadabar.stack.frontend.comm.rest.EntityRestComm
import zakadabar.stack.util.UUID

/**
 * Stores frontend objects that support an entity type. Instances of
 * [FrontendEntitySupport] are added to the
 * [FrontendContext][zakadabar.stack.frontend.FrontendContext] by module loaders.
 *
 * @param     module       The module that provides this entity type.
 * @property  companion    The companion object of the entity DTO.
 * @property  comm         The [EntityRestComm] for the DTO (defaults to standard RestComm for the companion).
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
    val comm: EntityRestCommContract<T>? = EntityRestComm(companion.type, companion.serializer()),
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