/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.folder

import zakadabar.stack.data.FolderDto
import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.navigator.EntityNavigator
import zakadabar.stack.frontend.builtin.desktop.navigator.NewEntity
import zakadabar.stack.frontend.builtin.desktop.navigator.NewEntityItemWithName
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.extend.NewEntityContract
import zakadabar.stack.util.UUID

class NewFolder(newEntity: NewEntity) : NewEntityItemWithName(newEntity) {

    companion object : NewEntityContract() {

        override val uuid = UUID("39fb543c-7f6f-4102-a8ba-d4d372a0e385")

        override val name by lazy { t(FolderDto.type) }

        override val target = EntityNavigator.newEntity

        override val icon = Icons.folder.simple18

        override fun newInstance(scope: Any?) = NewFolder(scope as NewEntity)

    }

    override suspend fun create(parentDto: EntityRecordDto?, name: String) {

        val parentId = parentDto?.id

        val dto = FolderDto(id = 0, entityRecord = EntityRecordDto.new(parentId, FolderDto.type, name))

        dto.create()

    }

}
