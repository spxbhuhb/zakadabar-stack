/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.folder

import zakadabar.stack.data.builtin.FolderDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.builtin.desktop.navigator.NewEntity
import zakadabar.stack.frontend.builtin.desktop.navigator.NewEntityItemWithName
import zakadabar.stack.frontend.extend.ViewCompanion

class NewFolder(newEntity: NewEntity) : NewEntityItemWithName(newEntity) {

    companion object : ViewCompanion {

        override fun newInstance(scope: Any?) = NewFolder(scope as NewEntity)

    }

    override suspend fun create(parentDto: EntityRecordDto?, name: String) {

        val parentId = parentDto?.id

        val dto = FolderDto(id = 0, entityRecord = EntityRecordDto.new(parentId, FolderDto.type, name))

        dto.create()

    }

}
