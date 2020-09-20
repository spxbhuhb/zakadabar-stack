/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.entity

import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.extend.DtoFrontend

object EntityRecordFrontend : DtoFrontend<EntityRecordDto>(
    Stack.uuid,
    EntityRecordDto.Companion
) {
    override val dtoClass = EntityRecordDto::class
}