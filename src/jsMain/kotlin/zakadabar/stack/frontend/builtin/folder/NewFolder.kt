/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.folder

import zakadabar.stack.data.FolderDto
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.messages.EntityChildrenLoaded
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.navigator.EntityNavigator
import zakadabar.stack.frontend.builtin.navigator.NewEntity
import zakadabar.stack.frontend.builtin.navigator.NewEntityItemWithName
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

    override suspend fun create(parentDto: EntityDto?, name: String) {

        val parentId = parentDto?.id

        val entityDto = FolderDto(id = 0, entityDto = EntityDto.new(parentId, FolderDto.type, name))

        entityDto.create()

        FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }

    }

}
