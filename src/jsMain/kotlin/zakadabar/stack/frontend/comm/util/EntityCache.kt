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

package zakadabar.stack.frontend.comm.util

import org.w3c.files.Blob
import org.w3c.files.File
import zakadabar.stack.Stack
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.builtin.desktop.messages.EntityChildrenLoaded
import zakadabar.stack.frontend.comm.rest.CachedRestComm
import zakadabar.stack.frontend.errors.FetchError
import zakadabar.stack.frontend.util.launch
import zakadabar.stack.frontend.util.log
import zakadabar.stack.util.PublicApi

@PublicApi
object EntityCache : CachedRestComm<EntityDto>("${Stack.shid}/entities", EntityDto.serializer(), childInfo = true) {

    @PublicApi
    fun launchCreateAndPush(parentId: Long?, file: File) {
        launch {
            val dto = create(EntityDto.new(parentId, file.name, file.type))

            pushBlob(dto.id, file)

            FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }
        }
    }

    @PublicApi
    fun launchCreateAndPush(parentId: Long?, name: String, type: String, byteArray: ByteArray) {
        launch {
            val dto = create(EntityDto.new(parentId, name, type))

            pushBlob(dto.id, Blob(byteArray.toTypedArray()))

            FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }
        }
    }

    @PublicApi
    fun launchGetChildren(parentId: Long?) {
        launch {

            if (children.containsKey(parentId)) {

                FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }

                return@launch
            }

            try {

                getChildren(parentId)

                FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId) }

            } catch (ex: Throwable) {
                log(ex)
                val status = if (ex is FetchError) ex.response.status.toString() else "unknown"

                FrontendContext.dispatcher.postSync { EntityChildrenLoaded(parentId, status) }
            }
        }
    }
}